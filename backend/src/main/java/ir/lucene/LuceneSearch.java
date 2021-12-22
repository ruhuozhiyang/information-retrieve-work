package ir.lucene;

import ir.entity.HotNews;
import ir.entity.IREntity;
import ir.entity.NewsItemForIndex;
import ir.entity.SearchReturn;
import ir.mapper.LuceneSearchMapper;
import ir.utils.DeleteDir;
import ir.utils.GetNewsFromTxt;
import ir.utils.HighLightWord;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.Resource;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

@Service
public class LuceneSearch {

  @Value("${pageSize}")
  private int pageSize;

  @Value("${index.store.path}")
  private String indexStorePath;

  @Value("${for.search.files}")
  private String forSearchFiles;

  @Value("${hot_news_count}")
  private int HotNewsCount;

  @Resource
  private LuceneSearchMapper lSMapper;

  @Autowired
  private SearchPredict sP;

  private List<HotNews> hot_news = new ArrayList<>(HotNewsCount);
  private List<Long> heat_t_l = new ArrayList<>(HotNewsCount);

  private void SetHotNewsObj(long t_h, String heat, String title, String url) {
    if (hot_news.size() < HotNewsCount) {
      hot_news.add(new HotNews(heat, title, url));
      heat_t_l.add(t_h);
    } else {
      Long min_h = Collections.min(heat_t_l);
      if (t_h > min_h) {
        for (HotNews e: hot_news) {
          if (e.getHeat().equals(String.valueOf(min_h))) {
            heat_t_l.remove(heat_t_l.indexOf(min_h));
            heat_t_l.add(t_h);
            hot_news.remove(e);
            hot_news.add(new HotNews(heat, title, url));
            break;
          }
        }
      }
    }
  }

  /**
   * 创建索引.
   * @return 是否创建成功.
   * @throws Exception exception.
   */
  public Boolean createIndex() throws Exception {
    Random r = new Random();
	  DeleteDir.DeleteDir(indexStorePath);
    Analyzer analyzer = new IKAnalyzer();
    BM25Similarity BMSim=new BM25Similarity();
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer).setSimilarity(BMSim));
    File dir = new File(forSearchFiles);
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file: files) {
        String filepath = file.getPath();
        List<NewsItemForIndex> newsItems = GetNewsFromTxt.GetNewsObject(filepath);
        for(int i=0; i < newsItems.size(); i++) {
        	String title = newsItems.get(i).getTitle();
        	String content = newsItems.get(i).getContent();
        	String url = newsItems.get(i).getUrl();
        	String time = newsItems.get(i).getTime().split(" ")[0].equals("")
              ? newsItems.get(i).getTime().split(" ")[1]
              : newsItems.get(i).getTime().split(" ")[0];
        	String source_website = newsItems.get(i).getSource_website();

        	Field website_url = new TextField("url", url, Store.YES);
        	Field website_title = new TextField("title", title, Store.YES);
        	Field website_content = new TextField("content", content, Store.YES);
          Field website_time = new SortedDocValuesField("time", new BytesRef(time));
          Field website_source = new TextField("source", source_website, Store.YES);
          long heat = r.nextLong();
          while (heat < 0) {
            heat = r.nextLong();
          }
          Field website_heat = new NumericDocValuesField("heat", heat);
          SetHotNewsObj(heat, String.valueOf(heat), title, url);
        	Document document = new Document();
        	document.add(website_url);
        	document.add(website_title);
        	document.add(website_content);
        	document.add(website_time);
        	document.add(new StoredField("time", time));
        	document.add(website_source);
        	document.add(website_heat);
        	document.add(new StoredField("heat", heat));
        	indexWriter.addDocument(document);
        }
      }
    }
    indexWriter.close();
    lSMapper.RecordHotNews(hot_news);
    return true;
  }

  /**
   * 查询内容.
   * @param field 查询的域.
   * @param content 搜索内容.
   * @param page 起始页.
   * @param sort_s 排序标准, 按r, t, h.
   * @return 搜索结果.
   * @throws IOException io exception.
   * @throws InvalidTokenOffsetsException InvalidTokenOffsetsException.
   * @throws ParseException ParseException.
   */
  public SearchReturn searchIndex(String field, String content, int page, String sort_s)
      throws IOException, InvalidTokenOffsetsException, ParseException {
    long t1 = new Date().getTime();
    Query query;
    char wildCardFlag = (char) (content.indexOf('*') > -1 ? 1 : 0);
    Analyzer analyzer = new IKAnalyzer();
	  BM25Similarity BMSim = new BM25Similarity();
	  String[] multiDefaultFields;
    String[] tempMultiDefaultFields = field == "all" ? new String[]{"title", "content"} : new String[]{field};

    multiDefaultFields = tempMultiDefaultFields;
    MultiFieldQueryParser mfQueryParser = new MultiFieldQueryParser(multiDefaultFields, analyzer);
	  //mfQueryParser.setDefaultOperator();
    List<IREntity> ResultList = new ArrayList<>();
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    indexSearcher.setSimilarity(BMSim);
    query = mfQueryParser.parse(content);
    TopDocs topDocs;
    if (sort_s.equals("t")) {
      topDocs = indexSearcher.search(query, page * pageSize, new Sort(new SortField("time", Type.STRING, true)));
    } else if (sort_s.equals("h")) {
      topDocs = indexSearcher.search(query, page * pageSize, new Sort(new SortField("heat", Type.LONG, true)));
    } else {
      topDocs = indexSearcher.search(query, page * pageSize);
    }

    // 取文档列表
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (int i = (page - 1) * pageSize; i < scoreDocs.length; i++) {
      //根据文档id获取文档
      Document document = indexSearcher.doc(scoreDocs[i].doc);
      String fileUrl = document.get("url");
      String fileTitle = document.get("title");
      String fileContent = document.get("content");
      String time = document.get("time");
      String source_website = document.get("source");
      String heat = document.get("heat");
      String fileSummary;
      if(wildCardFlag == 1) {
    	  String temp_content = content.replace('*', ' ');
    	  Query tempQuery = mfQueryParser.parse(temp_content);
    	  fileSummary = HighLightWord.getHighLightStringWild(tempQuery, analyzer, "content", fileContent, 100);
    	  fileTitle = HighLightWord.getHighLightStringWild(tempQuery, analyzer, "title", fileTitle, fileTitle.length());
      } else {
    	  fileSummary = HighLightWord.getHighLightString(query, analyzer, "content", fileContent, 100);
    	  fileTitle = HighLightWord.getHighLightString(query, analyzer, "title", fileTitle, fileTitle.length());
      }
      ResultList.add(IREntity.builder().title(fileTitle).url(fileUrl).heat(heat)
          .summary(fileSummary).time(time).source_website(source_website).build());
    }
    indexReader.close();
    long t2 = new Date().getTime();
    String cost_time = String.valueOf(t2 - t1);
    return SearchReturn.builder().count(topDocs.totalHits).irEntities(ResultList).time(cost_time)
        .relSearch(sP.relevantSuggest(content, 10)).build();
  }

  /**
   * 获取前HotNewsCount名热度新闻.
   * @return list.
   */
  public List<HotNews> GetHotNews() {
    return lSMapper.GetHotNews();
  }

  /**
   * 记录搜索内容.
   * @param c 搜索内容.
   * @param t 时间.
   */
  public void RecordSearch(String c, String t) {
    if (lSMapper.If_E(c) > 0) {
      lSMapper.IncSearchCount(c, t);
    } else {
      lSMapper.RecordSearch(c, t);
    }
  }
}
