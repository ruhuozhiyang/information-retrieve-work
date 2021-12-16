package ir.controller;

import ir.common.Message;
import ir.lucene.LuceneSearch;
import ir.lucene.SearchPredict;
import java.io.IOException;
import java.util.Map;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
public class ForSearch extends BaseController {

  @Autowired
  private LuceneSearch luceneSearch;

  @Autowired
  private SearchPredict searchPredict;

  @PostMapping(value = "/search")
  public Message IRetrieve(@RequestBody Map<String, String> irEntity)
      throws IOException, InvalidTokenOffsetsException, ParseException {
    String search_content = irEntity.get("c");
    int page = new Integer(irEntity.get("p"));
    String sort_s = irEntity.get("s");
    String t = irEntity.get("t");
    luceneSearch.RecordSearch(search_content, t);
    return super.buildRestResult(luceneSearch.searchIndex("title", search_content, page, sort_s));
  }


  @GetMapping(value = "/hot-search")
  public Message HotSearch() {
    return super.buildRestResult(luceneSearch.GetHotNews());
  }

  @GetMapping(value = "/complete-predict")
  public Message SearchPredict(@RequestParam String q) throws IOException {
    return super.buildRestResult(searchPredict.SearchPredict(q));
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

  @GetMapping(value = "/create-phrase-sug")
  public Message CreatePhraseSug() {
    try {
      Boolean ifSuccess = false;
      if (searchPredict.createPhraseIndex()) {
        ifSuccess = searchPredict.createSuggestIndex();
      }
      return super.buildRestResult(ifSuccess, ifSuccess ? 1 : 0, ifSuccess ? "创建成功" : "创建失败", null);
    } catch (Exception e) {
      e.printStackTrace();
      return super.buildRestResult(false, 0, "创建失败", null);
    }
  }
}
