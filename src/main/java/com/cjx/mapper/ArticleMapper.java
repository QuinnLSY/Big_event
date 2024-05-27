package com.cjx.mapper;

import com.cjx.pojo.Article;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ArticleMapper {
    // 添加文章
    @Insert({"insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time) values (#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})"})
    void add(Article article);
    // 查询文章
    List<Article> list(Integer userId, Integer categoryId, String state);
    // 根据id查询文章
    @Select({"select * from article where id=#{id}"})
    Article findById(Integer id);
    // 更新文章
    @Update({"update article set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state},category_id=#{categoryId},update_time=#{updateTime} where id=#{id}"})
    void update(Article article);
    // 删除文章
    @Delete({"delete from article where id=#{id}"})
    void delete(Integer id);
}
