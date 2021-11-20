package ir.utils;

import ir.entity.NewsItem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetNewsFromTxt {


  public static NewsItem GetNewsObject(String filePath) throws IOException {
    NewsItem newsItem = new NewsItem();
    File fileName = new File(filePath);
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    int count = 0;
    String content = "";
    String line = bufferedReader.readLine();;
    while (!line.equals("end")) {
      if (count == 0) {
        newsItem.setUrl(line);
      }
      if (count == 1) {
        newsItem.setTitle(line);
      }
      if (count >= 2 & !line.equals("start")) {
        content += line;
      }
      count ++;
      line = bufferedReader.readLine();
    }
    newsItem.setContent(content);
    return newsItem;
  }

}
