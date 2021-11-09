package ir.utils;

import ir.entity.NewsItem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.util.StringUtils;

public class GetNewsFromTxt {


  public static NewsItem GetNewsObject(String filePath) throws IOException {
    NewsItem newsItem = new NewsItem();
    File fileName = new File(filePath);
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    String line = "";
    int count = 0;
    while (!StringUtils.isEmpty(line = bufferedReader.readLine())) {
      if (count == 0) {
        newsItem.setUrl(line);
      }
      if (count == 1) {
        newsItem.setTitle(line);
      }
      if (count == 2) {
        newsItem.setContent(line);
      }
      count ++;
    }
    return newsItem;
  }

  public static void main(String[] args) throws IOException {
    String filePath = "/Users/foiunclekay/Documents/GitHub/news_spider_scrapy/news_spider_scrapy/news_spider_scrapy/result_news/1.txt";
    System.out.println(GetNewsFromTxt.GetNewsObject(filePath).toString());
  }

}
