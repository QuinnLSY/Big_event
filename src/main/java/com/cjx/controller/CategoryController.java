package com.cjx.controller;

import com.cjx.pojo.Category;
import com.cjx.pojo.Result;
import com.cjx.service.CategoryService;
import com.cjx.utils.ThreadLocalUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类别控制器，负责处理与类别相关的HTTP请求
 */
@RestController
@RequestMapping({"/category"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService; // 类别服务注入

    /**
     * 添加一个新的类别
     * @param category 待添加的类别对象，需要通过验证。该参数通过RequestBody接收前端传来的JSON数据，并且必须通过Category.Add类的验证。
     * @return 添加结果，成功返回添加的类别信息，失败返回错误信息。返回的结果封装在Result对象中，其中包含了操作是否成功及相应的消息。
     */
    @PostMapping
    public Result add(@RequestBody @Validated({Category.Add.class}) Category category) {
        // 检查是否存在同名类别
        List<Category> c = categoryService.findByCategoryName(category.getCategoryName());

        // 使用ThreadLocalUtil获取当前线程的本地存储map
        Map<String, Object> map = ThreadLocalUtil.get();
        Iterator i = c.iterator(); //iterator() 是一个方法，它是 Java 中所有集合接口（如 List, Set, Queue 等）的一个成员方法。当你调用 iterator() 方法时，它会返回一个实现了 Iterator 接口的对象。这个 Iterator 对象可以用来遍历集合中的元素，依次访问它们而不需要知道底层的实现细节。
        // 遍历检查结果，看是否有同名且由当前用户创建的类别,do/while语句
        Category c1;
        do {
            // 遍历检查结果，看是否有同名且由当前用户创建的类别
            if (!i.hasNext()) {
                // 未找到同名类别，进行添加
                categoryService.add(category);

                // 再次查询以确认添加结果
                List<Category> c2 = categoryService.findByCategoryName(category.getCategoryName());
                Iterator i2 = c2.iterator();

                Category c3;
                do {
                    // 确认添加是否成功，确保返回的类别信息是正确的
                    if (!i2.hasNext()) {
                        // 添加失败
                        return Result.fail("添加失败");
                    }

                    c3 = (Category)i2.next();
                } while(c3 == null || !c3.getCategoryName().equals(category.getCategoryName()));

                // 添加成功
                return Result.success(c3);
            }

            c1 = (Category)i.next();
        } while(c1 == null || !c1.getCreateUser().equals(map.get("id")));

        // 用户已创建此分类，返回失败结果
        return Result.fail("该用户已创建此分类");
    }


    /**
     * 获取所有类别列表的接口
     * 该接口不需要接收任何参数，仅用于从服务器获取所有的类别列表信息。
     *
     * @return Result<List<Category>> 类别列表的结果封装对象，其中包含操作状态和类别列表数据。
     */
    @GetMapping
    public Result<List<Category>> list() {
        // 通过categoryService获取所有的类别列表
        List<Category> cs = categoryService.list();
        // 将获取到的类别列表封装到Result对象中并返回
        return Result.success(cs);
    }


    /**
     * 根据ID获取类别详情
     * <p>
     * 本方法用于根据提供的ID查询特定的类别详情。如果类别存在，则返回该类别的详细信息；如果类别不存在，则返回错误信息。
     * </p>
     * @param id 类别的ID，用于标识特定的类别。
     * @return 返回类别详情结果。如果类别存在，则返回包含该类别信息的Result对象；如果类别不存在，则返回一个包含错误信息的Result对象。
     */
    @GetMapping({"/detail"})
    public Result<Category> detail(Integer id) {
        // 通过ID查找类别
        Category c = categoryService.findById(id);
        // 如果找到，则返回类别信息；否则，返回错误信息
        return c == null ? Result.fail("分类不存在") : Result.success(c);
    }


    /**
     * 更新一个类别信息
     * @param category 待更新的类别对象，需要通过验证。该参数通过RequestBody接收前端传来的JSON数据，并且必须通过Category.Update验证注解进行合法性校验。
     * @return 更新结果，成功返回更新后的类别信息，失败返回错误信息。返回的结果封装在Result对象中，其中包含了操作是否成功及相应的信息。
     */
    @PutMapping
    public Result update(@RequestBody @Validated({Category.Update.class}) Category category) {
        // 根据传入的ID查找分类是否存在
        Category c = categoryService.findById(category.getId());
        if (c == null) {
            // 分类不存在时返回错误信息
            return Result.fail("分类不存在");
        } else {
            // 从线程本地存储中获取当前用户信息
            Map<String, Object> map = ThreadLocalUtil.get();
            // 检查当前用户是否有权限修改该分类
            if (!c.getCreateUser().equals(map.get("id"))) {
                // 若无权限修改，则返回错误信息
                return Result.fail("无权限修改");
            } else {
                // 具有权限，进行修改
                categoryService.update(category);
                // 更新成功后，再次获取并返回更新后的分类信息
                Category c1 = categoryService.findById(category.getId());
                return Result.success(c1);
            }
        }
    }


    /**
     * 根据ID删除一个类别
     * @param id 类别的ID
     * @return 删除结果，成功返回删除成功的消息，失败返回错误信息
     */
    @DeleteMapping
    public Result delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get(); // 获取当前线程的本地存储map，通常用于存储用户信息等

        // 检查该类别是否存在
        if (categoryService.findById(id) == null) {
            // 分类不存在
            return Result.fail("分类不存在");
        } else if (!categoryService.findById(id).getCreateUser().equals(map.get("id"))) {
            // 检查当前用户是否有权限删除该类别
            Category c = categoryService.findById(id);
            return Result.fail("无权限删除,权限属于：" + c.getCreateUser());
        } else {
            // 具有权限，进行删除操作
            categoryService.delete(id);
            return Result.success("删除成功");
        }
    }

}

