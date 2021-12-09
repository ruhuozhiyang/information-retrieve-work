package ir.lucene;

import ir.mapper.LuceneSearchMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilterFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.util.FilesystemResourceLoader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SearchPredict {

  @Value("${index.store.path}")
  private String indexStorePath;
  @Value("${complete_weight_h}")
  private float c_w_h;
  @Value("${complete_count}")
  private int complete_count;
  @Resource
  private LuceneSearchMapper lMapper;

  private final String p_f = "title";

  private List<String> GetIndexPredict(Query query, String q, List<String> p) throws IOException {
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    TopDocs topDocs = indexSearcher.search(query, complete_count - p.size());
    for(ScoreDoc scoreDoc : topDocs.scoreDocs) {
      String t_c = indexSearcher.doc(scoreDoc.doc).get(p_f);
      int b_i = t_c.toLowerCase().indexOf(q.toLowerCase());
      if (b_i > -1) p.add(t_c.substring(b_i, b_i + 5 >= t_c.length() ? t_c.length() -1 : b_i + 5));
    }
    indexReader.close();
    return p;
  }

  public List<String> SearchPredict(String q) throws IOException {
    // 优先看一看搜索历史
    // 要兼顾搜索较多的词条
    // 同义词
    // 固有的行业词条 不同的权重
    List<String> pre_l = new ArrayList<>();
    List<Map<String, Object>> l_m = lMapper.GetCompleteFromSql(q);
    if (l_m.size() > 0) {
      Iterator<Map<String, Object>> it = l_m.iterator();
      while (it.hasNext()) {
        pre_l.add((String) it.next().get("content"));
      }
    }
    if (pre_l.size() < complete_count) {
      Query query = new PrefixQuery(new Term(p_f, q));
      List<String> g_i_p = GetIndexPredict(query, q, pre_l);
      return g_i_p;
    }
    return pre_l;
  }

  private static List<String> GetSynonyms(String q) throws IOException {
    String path = "./synonyms.txt";
    Version ver = Version.LUCENE_7_7_3;
    Map<String, String> filterArgs = new HashMap<>();
    filterArgs.put("luceneMatchVersion", ver.toString());
    filterArgs.put("synonyms", path);
    filterArgs.put("expand", "true");
    SynonymGraphFilterFactory factory = new SynonymGraphFilterFactory(filterArgs);
    factory.
        inform(new FilesystemResourceLoader(Paths.get("./"),
        (ClassLoader) null));
    WhitespaceAnalyzer whitespaceAnalyzer = new WhitespaceAnalyzer();
    TokenStream ts = factory.create(whitespaceAnalyzer.tokenStream("someField", q));

    List<String> tongList = new ArrayList<>();
    CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
    OffsetAttribute offsetAttribute = ts.addAttribute(OffsetAttribute.class);
    ts.reset();
    while (ts.incrementToken())
    {
      String token = termAttr.toString();
      tongList.add(token);
      System.out.println(token);
      System.out.print(offsetAttribute.startOffset() + "-" + offsetAttribute.endOffset() + "[" + token + "] ");
    }
    ts.end();
    ts.close();
    return tongList;
  }

  public static void main(String[] args) throws IOException {
//    GetSynonyms("其实 hankcs 似 好人");
  }
}
