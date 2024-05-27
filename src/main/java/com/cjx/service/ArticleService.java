package com.cjx.service;

import com.cjx.pojo.Article;
import com.cjx.pojo.PageBean;

public interface ArticleService {
    // 添加文章
    void add(Article article);
    // 分页查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
    // 根据id查询文章
    Article findById(Integer id);
    // 更新文章
    void update(Article article);
    // 删除文章
    void delete(Integer id);


}
