package ir.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IREntity extends HotNews {

  private String source_website; // 新闻的来源网站.
  private String summary; // 新闻的概述.
  private String time; // 新闻的发布时间.
  private String author; // 新闻的作者.
  private String relevance; // 相关度.

  @Builder
  IREntity(String heat, String title, String url, String source_website,
      String summary, String time, String author, String relevance) {
    super(heat, title, url);
    this.source_website = source_website;
    this.summary = summary;
    this.time = time;
    this.author = author;
    this.relevance = relevance;
  }
}
