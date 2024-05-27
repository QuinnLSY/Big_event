package com.cjx.validation;

import com.cjx.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 实现State注解的验证器，用于验证字符串是否为"已发布"或"草稿"状态。
 */
public class StateValidation implements ConstraintValidator<State, String> {
    /**
     * 验证给定的字符串是否符合"已发布"或"草稿"状态。
     *
     * @param value 要验证的字符串状态。
     * @param context 验证上下文，提供关于验证过程的上下文信息。
     * @return boolean 返回验证结果，如果字符串为"已发布"或"草稿"，则返回true；否则返回false。
     */
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 验证字符串是否为空
        if (value == null) {
            return false;
        } else {
            // 检查字符串是否等于"已发布"或"草稿"
            return value.equals("已发布") || value.equals("草稿");
        }
    }
}
