package com.cjx.service.impl;

import com.cjx.mapper.ArticleMapper;
import com.cjx.pojo.Article;
import com.cjx.pojo.PageBean;
import com.cjx.service.ArticleService;
import com.cjx.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章服务的实现类，负责处理文章相关的业务逻辑。
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 添加一篇文章。
     * @param article 待添加的文章对象，需要设置创建时间和更新时间，以及创建用户信息。
     */
    @Override
    public void add(Article article) {
        // 设置文章的创建时间和更新时间
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        // 获取当前线程的用户信息，并设置到文章中
        Map<String, Object> map = ThreadLocalUtil.get();
        article.setCreateUser((Integer)map.get("id"));
        // 调用mapper层添加文章
        articleMapper.add(article);
    }

    /**
     * 分页查询文章列表。
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param categoryId 分类ID，可选，用于筛选特定分类的文章
     * @param state 状态，可选，用于筛选特定状态的文章
     * @return 返回分页后的文章列表信息
     */
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        // 使用PageHelper进行分页
        PageBean<Article> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        // 获取当前线程的用户ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer)map.get("id");
        // 调用mapper层查询满足条件的文章列表
        List<Article> as = articleMapper.list(userId, categoryId, state);
        // 将Page对象的结果设置到PageBean中
        Page<Article> p = (Page<Article>)as;
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    /**
     * 根据ID查询文章详情。
     * @param id 文章ID
     * @return 返回对应ID的文章对象，如果不存在则返回null。
     */
    @Override
    public Article findById(Integer id) {
        // 调用mapper层查询指定ID的文章
        Article article = articleMapper.findById(id);
        return article;
    }

    /**
     * 更新一篇文章的信息。
     * @param article 待更新的文章对象，需要设置更新时间。
     */
    @Override
    public void update(Article article) {
        // 设置文章的更新时间
        article.setUpdateTime(LocalDateTime.now());
        // 调用mapper层更新文章信息
        articleMapper.update(article);
    }

    /**
     * 根据ID删除一篇文章。
     * @param id 文章ID
     */
    @Override
    public void delete(Integer id) {
        // 调用mapper层删除指定ID的文章
        articleMapper.delete(id);
    }
}
