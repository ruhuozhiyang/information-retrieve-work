package ir.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsItem {

  private String url;
  private String title;
  private String content;

  @Override
  public String toString() {
    return "NewsItem{" +
        "url='" + url + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
