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
    NewsItemForIndex NItem = null;
    File fileName = new File(filePath);
    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName));
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    int count = 0;
    //String content = "";
    String line = bufferedReader.readLine();
    while (line!=null) {
      if (count%3 == 0) {
    	 NItem=new NewsItemForIndex();
    	 NItem.setUrl(line);
      }
      if (count%3 == 1) {
         NItem.setTitle(line);
      }
      if (count%3==2) {
    	 NItem.setContent(line);
    	 newsItems.add(NItem);
      }
      count ++;
      line = bufferedReader.readLine();
    }
    return newsItems;
  }

}
