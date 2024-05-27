package com.cjx.controller;

import com.cjx.pojo.Result;
import com.cjx.pojo.User;
import com.cjx.service.UserService;
import com.cjx.utils.JwtUtil;
import com.cjx.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器类，负责处理用户相关的请求
 */
@RestController
@RequestMapping({"/user"})
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册接口。该方法用于处理用户注册请求。
     * @param username 用户名，必须是3到16个非空字符。通过@Pattern注解限制了用户名的格式。
     * @param password 密码，必须是3到16个非空字符。通过@Pattern注解限制了密码的格式。
     * @return 注册结果。如果注册成功，返回success；如果注册失败（如用户名已存在），返回fail及错误信息。
     */
    @PostMapping({"/register"})
    public Result register(@Pattern(regexp = "^\\S{3,16}$") String username, @Pattern(regexp = "^\\S{3,16}$") String password) {
        // 通过用户名查询用户是否存在
        User u = userService.findByUserName(username);
        if (u == null) {
            // 用户名不存在，进行注册
            userService.register(username, password);
            return Result.success();
        } else {
            // 用户名已存在，返回失败信息
            return Result.fail("用户名已存在");
        }
    }


    /**
     * 用户登录功能实现。
     * 通过对提供的用户名和密码进行验证，实现用户的登录逻辑。
     *
     * @param username 用户名，必须是3到16个非空字符。通过@Pattern注解进行验证。
     * @param password 密码，必须是3到16个非空字符。通过@Pattern注解进行验证。
     * @return Result对象，包含登录结果。成功返回success及生成的token，失败返回fail及错误信息。
     */
    @PostMapping({"/login"})
    public Result login(@Pattern(regexp = "^\\S{3,16}$") String username, @Pattern(regexp = "^\\S{3,16}$") String password) {
        // 根据用户名查找用户
        User loginUser = userService.findByUserName(username);
        if (loginUser == null) {
            // 用户名不存在时返回失败结果
            return Result.fail("用户名不存在");
        } else if (userService.checkPassword(password, loginUser.getPassword())) {
            // 用户名存在且密码正确时，生成token并返回成功结果
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            // 将token存储到Redis中，设置过期时间为1小时
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            ops.set(token, token, 1, TimeUnit.HOURS);

            return Result.success(token);
        } else {
            // 密码错误时返回失败结果
            return Result.fail("密码错误");
        }
    }


    /**
     * 获取当前登录用户的信息
     * 该接口不需要接收任何参数，通过当前线程的本地存储获取登录用户名，然后查询并返回对应用户的详细信息。
     * @return Result<User> 包含当前登录用户的详细信息的Result对象，如果查询成功，Result的status为200，data为User对象；如果失败，status为其他值，data为null或错误信息。
     */
    @GetMapping({"/userInfo"})
    public Result<User> userTnfo() {
        // 从ThreadLocal中获取当前登录用户的信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");

        // 根据用户名查询用户信息
        User user = userService.findByUserName(username);
        return Result.success(user); // 返回用户信息
    }


    /**
     * 更新用户信息
     *
     * @param user 包含更新信息的用户对象，通过请求体传入
     * @return 更新结果，成功返回success，失败返回fail。返回结果封装在Result对象中。
     */
    @PutMapping({"/update"})
    public Result update(@RequestBody User user) {
        // 调用userService的update方法更新用户信息
        userService.update(user);
        // 返回更新成功的结果
        return Result.success(user);
    }


    /**
     * 更新用户头像URL
     * @param avatarUrl 新的头像URL。该参数必须是一个有效的URL，用于指定用户的新头像位置。
     * @return 更新结果，成功返回success，失败返回fail。通过返回Result对象来表明头像更新操作的成功或失败。
     */
    @PatchMapping({"/updateAvatar"})
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        // 调用userService的updateAvatar方法，更新用户的头像URL
        userService.updateAvatar(avatarUrl);
        // 返回成功结果
        return Result.success();
    }


    /**
     * 更新用户密码
     * @param params 包含旧密码、新密码和确认新密码的Map
     * @param token 用户的token
     * @return 更新结果，成功返回success，失败返回fail及错误信息
     */
    @PatchMapping({"/updatePassword"})
    public Result updatePassword(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        // 从请求体中获取旧密码、新密码和确认新密码
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        // 验证参数完整性
        if (StringUtils.hasLength(oldPwd) && StringUtils.hasLength(newPwd) && StringUtils.hasLength(rePwd)) {
            // 从线程本地存储中获取用户信息
            Map<String, Object> map = ThreadLocalUtil.get();
            String username = (String)map.get("username");
            User loginUser = userService.findByUserName(username);

            // 验证旧密码是否正确
            if (!userService.checkPassword(oldPwd, loginUser.getPassword())) {
                return Result.fail("旧密码错误");
            } else if (!newPwd.equals(rePwd)) { // 验证新密码和确认密码是否一致
                return Result.fail("两次密码不一致");
            } else if (userService.checkPassword(newPwd, loginUser.getPassword())) { // 验证新密码是否与旧密码相同
                return Result.fail("新密码不能与旧密码相同");
            } else {
                // 更新密码并清除用户token
                userService.updatePassword(newPwd);
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                ops.getOperations().delete(token);
                return Result.success();
            }
        } else {
            // 参数为空时返回失败结果
            return Result.fail("参数不能为空");
        }
    }


    /**
     * 用户登出接口。
     * 本接口用于处理用户的登出请求，主要逻辑包括：
     * 1. 从Redis中删除用户的token，实现用户登出功能。
     * 2. 清除线程本地存储的用户相关信息。
     *
     * @param token 用户的token，用于标识请求的用户。该token通过请求头的Authorization字段获取。
     * @return 登出结果，成功时返回一个包含成功标识的结果对象。
     */
    @GetMapping({"/logout"})
    public Result logout(@RequestHeader("Authorization") String token) {
        // 从Redis中删除用户的token
        stringRedisTemplate.opsForValue().getOperations().delete(token);
        // 清除线程本地存储的用户信息
        ThreadLocalUtil.remove();
        // 返回登出成功的结果
        return Result.success();
    }

}
