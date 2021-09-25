package ir.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
public class ForSearch {

  @PostMapping(value = "/search")
  public String IRetrieve(@RequestBody Map<String, String> irEntity) {
    System.out.println("收到：" + irEntity.get("content"));
    return null;
  }
}
