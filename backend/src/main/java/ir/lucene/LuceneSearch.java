package ir.lucene;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class LuceneSearch {

//  @Value("${index.store.path}")
  private String indexStorePath = "/Users/foiunclekay/Desktop/indexStore";

//  @Value("${for.search.files}")
  private String forSearchFiles = "/Users/foiunclekay/Desktop/searchsource";

  @Test
  public void createIndex() throws Exception {
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
    File dir = new File(forSearchFiles);

    File[] files = dir.listFiles();
    for (File file:
    files) {
      String filename = file.getName();
      String filepath = file.getPath();
      String content = FileUtils.readFileToString(file, "utf-8");
      //文件的大小
      long fileSize = FileUtils.sizeOf(file);

      Field fieldName = new TextField("name", filename, Store.YES);
      Field fieldPath = new TextField("path", filepath, Store.YES);
      Field fieldContent = new TextField("content", content, Store.YES);
      Field fieldSize = new TextField("size", fileSize + "", Store.YES);

      Document document = new Document();
      document.add(fieldContent);
      document.add(fieldName);
      document.add(fieldPath);
      document.add(fieldSize);

      indexWriter.addDocument(document);
    }
    indexWriter.close();
  }

  @Test
  public void searchIndex() throws IOException {
    Directory directory = FSDirectory.open(new File(indexStorePath).toPath());
    IndexReader indexReader = DirectoryReader.open(directory);
    IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    Query query = new TermQuery(new Term("content", "spring"));
    TopDocs topDocs = indexSearcher.search(query, 10);
    System.out.println("查询结果的总记录数:" + topDocs.totalHits);

    // 取文档列表
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    for (ScoreDoc item:
    scoreDocs) {
      int docId = item.doc;

      //根据文档id获取文档
      Document document = indexSearcher.doc(docId);
      String filename = document.get("name");
      String content = document.get("content");
      String fileSize = document.get("size");
      String filePath = document.get("path");
      System.out.println("查询的具体结果为:");
      System.out.println("filename:" + filename);
      System.out.println("content:" + content);
      System.out.println("fileSize:" + fileSize);
      System.out.println("filePath:" + filePath);
      System.out.println("====================");
    }
    indexReader.close();
  }

}
