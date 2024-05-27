package com.cjx.exception;

import com.cjx.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，用于捕获Controller层抛出的异常，并返回统一格式的错误响应结果。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 捕获所有异常类型(Exception)。
     *
     * @param e 异常对象，表示发生的错误。
     * @return 返回一个表示操作失败的结果对象Result，其中包含错误信息。
     */
    @ExceptionHandler({Exception.class})
    public Result handleException(Exception e) {
        e.printStackTrace(); // 打印异常栈信息，便于问题排查
        // 返回失败结果，如果有错误信息则使用错误信息，否则默认为"操作失败"
        return Result.fail(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }
}
