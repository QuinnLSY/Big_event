package com.cjx.interceptors;

import com.cjx.utils.JwtUtil;
import com.cjx.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器，用于验证请求是否具有有效的令牌。
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate; // Redis模板用于存储和检索token

    /**
     * 在处理请求之前执行的拦截器方法。
     * 验证请求头中的令牌，并在验证成功后设置用户信息到ThreadLocal中。
     *
     * @param request  HttpServletRequest对象，代表客户端的HTTP请求
     * @param response HttpServletResponse对象，代表服务器对客户端的响应
     * @param handler  将要执行的处理器对象
     * @return boolean 如果验证成功，返回true，继续处理请求；如果验证失败，返回false，终止处理请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization"); // 从请求头中获取令牌

        try {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue(); // 获取Redis的值操作
            String redisToken = ops.get(token); // 从Redis中检索对应的令牌
            if (redisToken == null) {
                throw new RuntimeException();  // 如果没有找到对应的令牌，抛出异常
            } else {
                Map<String, Object> claims = JwtUtil.parseToken(token); // 解析令牌获取用户信息
                ThreadLocalUtil.set(claims); // 将用户信息设置到ThreadLocal中，以便在请求处理过程中访问
                return true;
            }
        } catch (Exception e) {
            response.setStatus(401); // 如果处理过程中发生异常，设置响应状态为401未授权
            return false;
        }
    }

    /**
     * 请求处理完成后执行的拦截器方法。
     * 主要用于清除ThreadLocal中设置的用户信息，避免内存泄露。
     *
     * @param request  HttpServletRequest对象，代表客户端的HTTP请求
     * @param response HttpServletResponse对象，代表服务器对客户端的响应
     * @param handler  处理器对象
     * @param ex       在处理请求时抛出的异常，可能为null
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove(); // 清除ThreadLocal中的用户信息
    }
}
