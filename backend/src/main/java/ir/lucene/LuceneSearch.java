package ir.lucene;

import ir.entity.IREntity;
import ir.entity.NewsItem;
import ir.utils.GetNewsFromTxt;
import ir.utils.HighLightWord;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LuceneSearch {

  @Value("${index.store.path}")
  private String indexStorePath;
//  private static String indexStorePath = "/Users/foiunclekay/Desktop/indexStore";

  @Value("${for.search.files}")
  private String forSearchFiles;
//  private static String forSearchFiles = "/Users/foiunclekay/Documents/GitHub/news_spider_scrapy/news_spider_scrapy/news_spider_scrapy/result_news/";

  private Analyzer analyzer = new StandardAnalyzer();

  @Test
  public void createIndex() throws Exception {
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
    File dir = new File(forSearchFiles);

    File[] files = dir.listFiles();
    if (files != null) {
      for (File file:
      files) {
        String filepath = file.getPath();
        NewsItem newsItem = GetNewsFromTxt.GetNewsObject(filepath);
        String title = newsItem.getTitle();
        String content = newsItem.getContent();
        String url = newsItem.getUrl();

        Field website_url = new TextField("url", url, Store.YES);
        Field website_title = new TextField("title", title, Store.YES);
        Field website_content = new TextField("content", content, Store.YES);

        Document document = new Document();
        document.add(website_url);
        document.add(website_title);
        document.add(website_content);

        indexWriter.addDocument(document);
      }
    }
    indexWriter.close();
  }

  @Test
  public List<IREntity> searchIndex(String field, String content)
      throws IOException, InvalidTokenOffsetsException {
    List<IREntity> ResultList = new ArrayList<>();
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    Query query = new TermQuery(new Term(field, content));
    TopDocs topDocs = indexSearcher.search(query, 10);
//    System.out.println("查询结果的总记录数:" + topDocs.totalHits);

    // 取文档列表
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (ScoreDoc item: scoreDocs) {
      int docId = item.doc;
      //根据文档id获取文档
      Document document = indexSearcher.doc(docId);
      String fileUrl = document.get("url");
      String fileTitle = document.get("title");
      fileTitle = HighLightWord.getHighLightString(query, analyzer, "title", fileTitle, fileTitle.length());
      ResultList.add(IREntity.builder().title(fileTitle).url(fileUrl).build());
    }
    indexReader.close();

    return ResultList;
  }

}
