package ir.utils;

import ir.entity.NewsItemForIndex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GetNewsFromTxt {

  public static List<NewsItemForIndex> GetNewsObject(String filePath) throws IOException {
    List<NewsItemForIndex> newsItems = new ArrayList<>();
    File fileName = new File(filePath);
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    int count = 0;
    String line = bufferedReader.readLine();
    String url = "", title = "", content = "";
    while (line!=null) {
      if (count%3 == 0) {
        url = line;
      }
      if (count%3 == 1) {
        title = line;
      }
      if (count%3==2) {
        content = line;
        newsItems.add(
            NewsItemForIndex.builder().url(url).title(title).content(content).build()
        );
      }
      count ++;
      line = bufferedReader.readLine();
    }
    return newsItems;
  }

}
