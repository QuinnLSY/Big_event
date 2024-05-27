package com.cjx.service;

import com.cjx.pojo.Category;
import java.util.List;

public interface CategoryService {
    // 根据分类名称查询
    List<Category> findByCategoryName(String categoryName);
    // 添加新分类
    void add(Category category);
    // 查询所有分类
    List<Category> list();
    // 根据id查询
    Category findById(Integer id);
    // 更新分类信息
    void update(Category category);
    // 删除分类
    void delete(Integer id);
}
