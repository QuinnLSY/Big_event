package com.cjx.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类，用于生成和解析JSON Web Token。
 */
public class JwtUtil {
    private static final String KEY = "cjx"; // 用于JWT签名的密钥
    /**
     * 生成JWT Token。
     *
     * <p>此函数用于生成一个包含指定声明信息的JWT（JSON Web Token）。JWT是一种用于在各方之间安全地传输信息的紧凑的、URL安全的格式。
     * 生成的JWT包含一个过期时间，并使用HMAC256算法签名以确保数据的完整性和防篡改性。</p>
     *
     * @param claims Token中携带的声明信息。声明是有关实体（通常是用户）的断言信息。可以包含任何需要的信息，如用户ID、用户名等。
     * @return 生成的JWT Token字符串。这个字符串可以安全地发送给客户端，客户端可以使用它来进行身份验证和授权。
     */
    public static String genToken(Map<String, Object> claims) {
        // 创建JWT, 设置过期时间，并使用指定算法签名
        return JWT.create().withClaim("claims", claims).withExpiresAt(new Date(System.currentTimeMillis() + 43200000L)).sign(Algorithm.HMAC256(KEY));
    }


    /**
     * 解析JWT Token，从中提取出声明信息。
     *
     * @param token 需要解析的JWT Token字符串。它是由三部分组成：头部、载荷（声明信息）和签名。
     *              这里将验证Token的有效性，并从载荷中提取出声明信息。
     * @return 一个Map对象，包含Token中携带的声明信息。这些信息通常包括用户ID、用户名等，
     *         具体内容取决于发方如何定义和编码JWT。
     */
    public static Map<String, Object> parseToken(String token) {
        // 验证JWT的有效性，并提取claims（声明信息）
        return JWT.require(Algorithm.HMAC256(KEY)) // 创建JWT的验证器，使用HMAC256算法
                  .build() // 构建验证器实例
                  .verify(token) // 验证JWT token
                  .getClaim("claims") // 获取名为"claims"的特定声明
                  .asMap(); // 将该声明以Map形式返回
    }

}
