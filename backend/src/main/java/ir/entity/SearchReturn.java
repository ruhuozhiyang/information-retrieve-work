package ir.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
/**
 * 新闻检索返回的信息汇总.
 */
public class SearchReturn {

  private String count; // 所有相关的结果的数目.
  private List<IREntity> irEntities; // 当前批次返回的信息列表.
  private String time; // 本次查询耗时.

}
