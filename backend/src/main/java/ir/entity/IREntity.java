package ir.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class IREntity {
  private String title; // 返回新闻的标题
  private String url;
}
