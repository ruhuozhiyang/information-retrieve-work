package ir.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotNews {

  private String heat; // 热度.
  private String title; // 返回新闻的标题.
  private String url; // 新闻的URL.

}
