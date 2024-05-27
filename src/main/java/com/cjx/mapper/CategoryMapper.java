package com.cjx.mapper;

import com.cjx.pojo.Category;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMapper {
    // 根据分类名称查询分类
    @Select({"select * from category where category_name=#{categoryName}"})
    List<Category> findByCategoryName(String categoryName);
    // 添加分类
    @Insert({"insert into category(category_name,category_alias,create_user,create_time,update_time) values(#{categoryName},#{categoryAlias},#{createUser},#{createTime},#{updateTime})"})
    void add(Category category);
    // 查询分类
    @Select({"select * from category where create_user=#{userId}"})
    List<Category> list(Integer userId);
    // 根据id查询分类
    @Select({"select * from category where id=#{id}"})
    Category findById(Integer id);
    // 修改分类
    @Update({"update category set category_name=#{categoryName},category_alias=#{categoryAlias},update_time=#{updateTime} where id=#{id}"})
    void update(Category category);
    // 删除分类
    @Update({"delete from category where id=#{id}"})
    void delete(Integer id);
}

