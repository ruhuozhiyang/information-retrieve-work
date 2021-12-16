package ir.lucene;

import ir.mapper.LuceneSearchMapper;
import ir.utils.DeleteDir;
import ir.utils.GetNewsFromTxt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.suggest.DocumentDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

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

  @Value("${phraseFiles}")
  private String phraseFiles;
  @Value("${prefixIndexStorePath}")
  private String prefixIndexStorePath;
  @Value("${phrIndexStorePath}")
  private String phrIndexStorePath;

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
      List<String> g_i_p = autoComplete(q, complete_count - pre_l.size(), pre_l);
      return g_i_p;
    }
    return pre_l;
  }

  /**
   * 对分词词典建立索引.
   * @return boolean.
   * @throws Exception exception.
   */
  public Boolean createPhraseIndex() throws Exception {
    DeleteDir.DeleteDir(phrIndexStorePath);
    Analyzer analyzer = new IKAnalyzer();
    Directory directory = FSDirectory.open(new File(phrIndexStorePath).toPath());
    IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
    File dir = new File(phraseFiles);
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file: files) {
        String filepath = file.getPath();
        List<String> phrItems = GetNewsFromTxt.GetPhraseObject(filepath);
        for(int i=0; i < phrItems.size(); i++) {
          Field phr_content = new TextField("content", phrItems.get(i), Store.YES);
          Document document = new Document();
          document.add(phr_content);
          indexWriter.addDocument(document);
        }
      }
    }
    indexWriter.close();
    return true;
  }

  /**
   * suggester建立索引.
   * @return boolean.
   * @throws Exception exception.
   */
  public Boolean createSuggestIndex() throws Exception {
    Analyzer analyzer = new IKAnalyzer();
    DeleteDir.DeleteDir(prefixIndexStorePath);
    Directory preDirectory=FSDirectory.open(new File(prefixIndexStorePath).toPath());
    Directory directory = FSDirectory.open(new File(phrIndexStorePath).toPath());
    AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(preDirectory, analyzer);
    IndexReader indexReader = DirectoryReader.open(directory);
    DocumentDictionary DocDiction=new DocumentDictionary(indexReader,"content","content");
    suggester.build(DocDiction.getEntryIterator());
    return true;
  }

  /**
   * 通过suggester获取自动补全.
   * @param content 搜索内容.
   * @param maxNum 返回的最大个数.
   * @return list.
   * @throws IOException io exception.
   */
  public List<String> autoComplete(String content, int maxNum, List<String> p) throws IOException
  {
    Analyzer analyzer = new IKAnalyzer();
    Directory preDirectory = FSDirectory.open(new File(prefixIndexStorePath).toPath());
    AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(preDirectory, analyzer);
    List<Lookup.LookupResult> lkResults = suggester.lookup(content, maxNum, true,true);
    for(int i=0; i < lkResults.size(); i++)
    {
      p.add(lkResults.get(i).toString());
    }
    return p;
  }

  /**
   * 通过前缀查询获得自动补全
   * @param content 查询内容.
   * @param maxNum 返回的最大个数.
   * @return list.
   * @throws IOException io exception.
   */
  public List<String> prefixQ(String content, int maxNum) throws IOException
  {
    List<String> result=new ArrayList();
    Query query;
    Term term = new Term("content", content);
    Directory directory = FSDirectory.open(new File(phrIndexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    query = new PrefixQuery(term);
    TopDocs topDocs = indexSearcher.search(query, maxNum);
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    long returnNum = (topDocs.totalHits < maxNum) ? topDocs.totalHits : maxNum;
    for (int i = 0; i < returnNum; i++) {
      int docId = scoreDocs[i].doc;
      Document document = indexSearcher.doc(docId);
      result.add(document.get("content"));
    }
    return result;
  }

  public List<String> relevantSuggest(String content, int maxNum)
      throws IOException, ParseException {
    List<String> result=new ArrayList();
    Analyzer analyzer = new IKAnalyzer();
    Directory directory = FSDirectory.open(new File(phrIndexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    QueryParser queryParser =new QueryParser("content",analyzer);
    Query query=queryParser.parse(content);
    TopDocs topDocs=indexSearcher.search(query, maxNum);
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    long returnNum=(topDocs.totalHits<maxNum)?topDocs.totalHits:maxNum;
    for (int i=0;i<returnNum; i++) {
      int docId = scoreDocs[i].doc;
      Document document = indexSearcher.doc(docId);
      result.add(document.get("content"));
    }
    return result;

  }
}
