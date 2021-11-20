package ir.controller;

import ir.common.Message;
import ir.entity.IREntity;
import ir.entity.SearchReturn;
import ir.lucene.LuceneSearch;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
public class ForSearch extends BaseController {

  @Autowired
  private LuceneSearch luceneSearch;

  @PostMapping(value = "/search")
  public Message IRetrieve(@RequestBody Map<String, String> irEntity)
      throws IOException, InvalidTokenOffsetsException {
//    System.out.println("收到：" + irEntity.get("content"));
    String search_content = irEntity.get("content");
    return super.buildRestResult(luceneSearch.searchIndex("title", search_content));
  }

  @GetMapping(value = "/hot-search")
  public String hotSearch() {
    return null;
  }

  @GetMapping(value = "/create-index")
  public Message CreateIndex() {
    try {
      luceneSearch.createIndex();
      return super.buildRestResult(true, 1, "创建成功", null);
    } catch (Exception e) {
      e.printStackTrace();
      return super.buildRestResult(false, 0, "创建失败", null);
    }
  }
}
