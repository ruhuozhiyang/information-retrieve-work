package ir.lucene;

import ir.entity.IREntity;
import ir.entity.NewsItemForIndex;
import ir.entity.SearchReturn;
import ir.utils.DeleteDir;
import ir.utils.GetNewsFromTxt;
import ir.utils.HighLightWord;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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

  public Boolean createIndex() throws Exception {
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
        for(int i=0;i<newsItems.size();i++) {
        	String title = newsItems.get(i).getTitle();
        	String content = newsItems.get(i).getContent();
        	String url = newsItems.get(i).getUrl();
        	String time = newsItems.get(i).getTime();
        	String source_website = newsItems.get(i).getSource_website();

        	Field website_url = new TextField("url", url, Store.YES);
        	Field website_title = new TextField("title", title, Store.YES);
        	Field website_content = new TextField("content", content, Store.YES);
          Field website_time = new TextField("time", time, Store.YES);
          Field website_source = new TextField("source", source_website, Store.YES);

        	Document document = new Document();
        	document.add(website_url);
        	document.add(website_title);
        	document.add(website_content);
        	document.add(website_time);
        	document.add(website_source);

        	indexWriter.addDocument(document);
        }
      }
    }
    indexWriter.close();
    return true;
  }

  public SearchReturn searchIndex(String field, String content, int page)
      throws IOException, InvalidTokenOffsetsException, ParseException {
    Query query;
    char wildCardFlag = (char) (content.indexOf('*') > -1 ? 1 : 0);
    Analyzer analyzer = new IKAnalyzer();
	  BM25Similarity BMSim=new BM25Similarity();
	  String[] multiDefaultFields;
    String[] tempMultiDefaultFields = field == "all" ? new String[]{"title", "content"} : new String[]{field};

    multiDefaultFields=tempMultiDefaultFields;
    MultiFieldQueryParser mfQueryParser = new MultiFieldQueryParser(multiDefaultFields, analyzer);
	  //mfQueryParser.setDefaultOperator();
	  long t1 = new Date().getTime();
    List<IREntity> ResultList = new ArrayList<>();
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    indexSearcher.setSimilarity(BMSim);
    query=mfQueryParser.parse(content);
    TopDocs topDocs = indexSearcher.search(query, page * pageSize);

    // 取文档列表
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (int i = (page - 1) * pageSize; i < scoreDocs.length; i++) {
      int docId = scoreDocs[i].doc;
      //根据文档id获取文档
      Document document = indexSearcher.doc(docId);
      String fileUrl = document.get("url");
      String fileTitle = document.get("title");
      String fileContent = document.get("content");
      String time = document.get("time");
      String source_website = document.get("source");
      String fileSummary;
      if(wildCardFlag == 1) {
    	  String temp_content= content.replace('*', ' ');
    	  Query tempQuery=mfQueryParser.parse(temp_content);
    	  fileSummary=HighLightWord.getHighLightStringWild(tempQuery, analyzer, "content", fileContent, 100);
          fileTitle = HighLightWord.getHighLightStringWild(tempQuery, analyzer, "title", fileTitle, fileTitle.length());
      } else {
    	  fileSummary=HighLightWord.getHighLightString(query, analyzer, "content", fileContent, 100);
    	  fileTitle = HighLightWord.getHighLightString(query, analyzer, "title", fileTitle, fileTitle.length());
      }
      ResultList.add(IREntity.builder().title(fileTitle).url(fileUrl)
          .summary(fileSummary).time(time).source_website(source_website).build());
    }
    indexReader.close();
    long t2 = new Date().getTime();
    String cost_time = String.valueOf(t2 - t1);
    return SearchReturn.builder().count(topDocs.totalHits).irEntities(ResultList).time(cost_time).build();
  }

}
