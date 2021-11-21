package ir.utils;

import ir.entity.NewsItemForIndex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetNewsFromTxt {


  public static NewsItemForIndex GetNewsObject(String filePath) throws IOException {
    File fileName = new File(filePath);
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    int count = 0;
    String content = null;
    String url = null;
    String title = null;
    String line = bufferedReader.readLine();;
    while (!line.equals("end")) {
      if (count == 0) {
        url = line;
      }
      if (count == 1) {
        title = line;
      }
      if (count >= 2 & !line.equals("start")) {
        content += line;
      }
      count ++;
      line = bufferedReader.readLine();
    }
    return NewsItemForIndex.builder().url(url).title(title).content(content).build();
  }

}
