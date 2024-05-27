package com.cjx.controller;

import com.cjx.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章列表控制器
 * 用于处理文章列表相关的请求
 * 在用户登录做检测时用，即未登录用户无法直接访问出登录和注册页面之外的任何相关页面
 */
@RestController
@RequestMapping({"/articleList"})
public class ArticleListController {

    /**
     * 获取文章列表的接口
     * 无参数
     * @return 返回一个包含文章列表信息的结果对象
     */
    @GetMapping({"/list"})
    public Result<String> list() {
        // 返回成功结果，包含文章列表的简单说明
        return Result.success("文章列表");
    }
}
