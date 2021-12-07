package ir.mapper;

import ir.entity.HotNews;
import ir.entity.IREntity;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LuceneSearchMapper {

  @Insert({
    "<script>",
    "insert into hot_news(heat, title, url) values ",
    "<foreach collection='hotNews' item='item' index='index' separator=','>",
    "(#{item.heat}, #{item.title}, #{item.url})",
    "</foreach>",
    "</script>"
  })
  Boolean RecordHotNews(@Param("hotNews") List<HotNews> hotNews);

  @Select(value = "select * from hot_news")
  List<HotNews> GetHotNews();
}
