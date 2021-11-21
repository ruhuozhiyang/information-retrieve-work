package ir.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NewsItemForIndex {

  private String url;
  private String title;
  private String content;

}
