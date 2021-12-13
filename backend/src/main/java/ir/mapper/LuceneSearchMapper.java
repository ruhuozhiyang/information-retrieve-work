package ir.mapper;

import ir.entity.HotNews;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

  @Insert(value = "insert into all_search_record(time, content, count) values (#{t}, #{c}, 1)")
  Boolean RecordSearch(@Param("c") String c, @Param("t") String t);

  @Select(value = "select count(1) from all_search_record where content = #{c}")
  int If_E(@Param("c") String c);

  @Update(value = "update all_search_record set count = count + 1, time = #{t} where content = #{c}")
  Boolean IncSearchCount(@Param("c") String c, @Param("t") String t);

  @Select(value = "select * from all_search_record where content like concat('%', #{c}, '%') limit 10")
  List<Map<String, Object>> GetCompleteFromSql(String c);

  @Select(value = "select content from all_search_record where content like concat('%',#{c},'%') limit 10")
  List<String> similaritySearch(String c);
}
