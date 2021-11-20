package ir.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class IREntity {

  private String title; // 返回新闻的标题.
  private String url; // 新闻的URL.
  private String source_website; // 新闻的来源网站.
  private String summary; // 新闻的概述.
  private String time; // 新闻的发布时间.
  private String author; // 新闻的作者.
  private String relevance; // 相关度.
  private String heat; // 热度.

}
