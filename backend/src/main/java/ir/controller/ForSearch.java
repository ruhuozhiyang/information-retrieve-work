package ir.controller;

import ir.common.Message;
import ir.lucene.LuceneSearch;
import java.io.IOException;
import java.util.Map;
import org.apache.lucene.queryparser.classic.ParseException;
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
      throws IOException, InvalidTokenOffsetsException, ParseException {
    String search_content = irEntity.get("content");
    int page = new Integer(irEntity.get("page"));
    System.out.println("收到：" + page);
    return super.buildRestResult(luceneSearch.searchIndex("title", search_content, page));
  }

  @GetMapping(value = "/hot-search")
  public String hotSearch() {
    return null;
  }

  @GetMapping(value = "/create-index")
  public Message CreateIndex() {
    try {
      Boolean ifSuccess = luceneSearch.createIndex();
      return super.buildRestResult(ifSuccess, ifSuccess ? 1 : 0, ifSuccess ? "创建成功" : "创建失败", null);
    } catch (Exception e) {
      e.printStackTrace();
      return super.buildRestResult(false, 0, "创建失败", null);
    }
  }
}
