package com.cjx.controller;

import com.cjx.pojo.Article;
import com.cjx.pojo.Category;
import com.cjx.pojo.PageBean;
import com.cjx.pojo.Result;
import com.cjx.service.ArticleService;
import com.cjx.service.impl.CategoryServiceImpl;
import com.cjx.utils.ThreadLocalUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章控制器，负责处理与文章相关的HTTP请求
 */
@RestController
@RequestMapping({"/article"})
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    /**
     * 添加文章
     * 本方法负责通过接收前端发送的请求，将文章对象添加到系统中。
     * @param article 待添加的文章对象，需要进行验证。该参数通过请求体接收，并且必须是一个经过验证的文章对象，以确保数据的完整性和正确性。
     * @return 添加结果，成功返回添加的文章信息，失败返回错误信息。通过Result对象返回操作结果，如果添加成功，返回成功标志和添加的文章信息；如果添加失败，返回错误信息。
     */
    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        // 将验证通过的文章对象添加到服务中
        articleService.add(article);
        // 返回添加成功的结果，包含添加的文章信息
        return Result.success(article);
    }


    /**
     * 获取文章列表的分页信息。该接口允许用户按照页码和每页数量查询文章列表，可选地根据分类ID和文章状态进行筛选。
     *
     * @param pageNum 请求的页码，表示需要返回的页码。
     * @param pageSize 每页的数量，指定每页返回的文章数量。
     * @param categoryId 分类ID，可选参数，用于按分类查询文章。如果未指定，则返回所有分类的文章。
     * @param state 文章状态，可选参数，用于按文章状态查询文章。如果未指定，则返回所有状态的文章。
     * @return 返回一个Result对象，其中包含文章列表的分页信息。如果查询成功，Result的data字段将包含PageBean<Article>对象；如果失败，Result的msg字段将包含错误信息。
     */
    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum, Integer pageSize,
                                          @RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String state) {
        // 调用articleService的服务方法，查询指定条件下的文章列表的分页信息
        PageBean<Article> pb = articleService.list(pageNum, pageSize, categoryId, state);
        // 将查询结果封装成Result对象返回
        return Result.success(pb);
    }


    /**
     * 获取文章详情
     * 这个方法用于根据文章ID查询文章的详细信息。
     * @param id 文章ID，用于查询特定文章的详细信息。
     * @return 返回一个结果对象，如果文章存在则包含文章详情，否则返回错误信息。
     */
    @GetMapping({"/detail"})
    public Result<Article> detail(Integer id) {
        // 根据文章ID查找文章
        Article article = articleService.findById(id);

        // 如果找到了文章，返回文章详情；否则，返回错误信息
        return article == null ? Result.fail("文章不存在") : Result.success(article);
    }


    /**
     * 更新文章信息
     * @param article 待更新的文章对象，需要进行验证。该参数通过RequestBody接收前端传来的JSON数据，并通过Validated注解进行合法性校验。
     * @return 更新结果，成功返回更新后的文章信息，失败返回错误信息。返回的结果封装在Result对象中，其中包含了操作是否成功及相应的信息。
     */
    @PutMapping
    public Result update(@RequestBody @Validated Article article) {
        Map<String, Object> map = ThreadLocalUtil.get(); // 从ThreadLocal中获取当前线程绑定的用户信息
        Integer userId = (Integer)map.get("id"); // 获取当前操作用户的ID
        Category category = categoryServiceImpl.findById(article.getCategoryId()); // 根据文章分类ID查找对应的分类信息

        // 检查分类是否存在以及用户是否有权限修改
        if (category == null) {
            return Result.fail("分类不存在"); // 如果分类不存在，则返回错误信息
        } else if (!userId.equals(category.getCreateUser())) {
            return Result.fail("无权限修改"); // 如果当前用户不是该分类的创建者，则返回无权限修改的错误信息
        } else {
            articleService.update(article); // 如果分类存在且用户有权限修改，则更新文章信息
            return Result.success(article); // 返回更新成功的文章信息
        }
    }


    /**
     * 删除文章
     * @param id 文章ID，用于指定要删除的文章
     * @return 删除结果，成功返回成功信息，失败返回错误信息
     */
    @DeleteMapping
    public Result delete(Integer id) {
        // 检查文章是否存在
        if (articleService.findById(id) == null) {
            return Result.fail("文章不存在");
        } else {
            // 获取当前用户信息，并检查是否有权限删除该文章
            Map<String, Object> map = ThreadLocalUtil.get();
            if (!map.get("id").equals(articleService.findById(id).getCreateUser())) {
                return Result.fail("无权限删除");
            } else {
                // 执行文章删除操作
                articleService.delete(id);
                return Result.success("成功删除文章！");
            }
        }
    }

}
