package ir.controller;

import ir.common.Message;
import ir.entity.IREntity;
import ir.lucene.LuceneSearch;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
  public Message IRetrieve(@RequestBody Map<String, String> irEntity) throws IOException {
    System.out.println("收到：" + irEntity.get("content"));
    String search_content = irEntity.get("content");
    List<IREntity> irEntities = luceneSearch.searchIndex("title", search_content);
    return super.buildRestResult(irEntities);
  }

  @GetMapping(value = "/hot-search")
  public String hotSearch() {
    return null;
  }
}
