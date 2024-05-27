package com.cjx.anno;

import com.cjx.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 定义一个名为State的注解，用于验证对象字段的状态是否合法。
 * 该注解适用于字段级别，并在运行时进行验证。
 * 需要配合实现类StateValidation一起使用，用于检查字段值是否为"已发布"或"草稿"状态。
 */
@Documented // 表示该注解会被Javadoc处理
@Target({ElementType.FIELD}) // 指定该注解适用于字段
@Retention(RetentionPolicy.RUNTIME) // 指定该注解在运行时可被读取
@Constraint(
        validatedBy = {StateValidation.class} // 指定用于验证的实现类
)
public @interface State {
    /**
     * 验证失败时的错误消息，默认为"state参数只能是已发布或草稿"
     * @return 错误消息字符串
     */
    String message() default "state参数只能是已发布或草稿";

    /**
     * 定义该注解适用的验证组，默认为空组
     * @return 验证组数组
     */
    Class<?>[] groups() default {};

    /**
     * 定义该注解关联的Payload类型，默认为空
     * @return Payload类型数组
     */
    Class<? extends Payload>[] payload() default {};
}
