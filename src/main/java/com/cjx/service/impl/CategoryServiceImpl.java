package com.cjx.service.impl;

import com.cjx.mapper.CategoryMapper;
import com.cjx.pojo.Category;
import com.cjx.service.CategoryService;
import com.cjx.utils.ThreadLocalUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类别服务的实现类，负责处理与类别相关的业务逻辑。
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper; // 自动注入类别映射器

    /**
     * 根据类别名称查找类别列表。
     *
     * @param categoryName 类别名称
     * @return 类别列表
     */
    @Override
    public List<Category> findByCategoryName(String categoryName) {
        List<Category> c = categoryMapper.findByCategoryName(categoryName);
        return c;
    }

    /**
     * 添加一个新的类别。
     * 这个方法会为待添加的类别对象设置创建者信息（通过当前线程的ThreadLocal获取用户ID）和创建/更新时间，
     * 然后将其添加到数据库中。
     *
     * @param category 待添加的类别对象。这个对象需要包含类别的详细信息，除了创建者信息和时间，
     *                 这些信息会在方法内部被设置。
     */
    @Override
    public void add(Category category) {
        // 从ThreadLocal中获取当前用户ID，并设置到类别对象中作为创建者ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer)map.get("id");
        category.setCreateUser(userId);
        // 设置创建时间和更新时间为当前时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        // 调用categoryMapper的add方法，将类别对象添加到数据库
        categoryMapper.add(category);
    }


    /**
     * 获取所有类别列表。
     * 这个方法重写了list方法，目的是为了根据当前线程中的用户ID，获取该用户相关的所有类别列表。
     *
     * @return 类别列表，返回的是一个Category类型的List集合。
     */
    @Override
    public List<Category> list() {
        // 从ThreadLocal中获取用户ID，用于后续可能的过滤或关联查询
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer)map.get("id");
        // 根据用户ID，调用categoryMapper获取类别列表
        return categoryMapper.list(userId);
    }


    /**
     * 根据ID查找类别。
     *
     * @param id 类别的ID
     * @return 找到的类别对象
     */
    @Override
    public Category findById(Integer id) {
        Category c = categoryMapper.findById(id);
        return c;
    }

    /**
     * 更新一个类别信息。
     *
     * @param category 待更新的类别对象
     */
    @Override
    public void update(Category category) {
        // 更新类别更新时间
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    /**
     * 根据ID删除一个类别。
     *
     * @param id 类别的ID
     */
    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}
