package com.cjx.service.impl;

import com.cjx.mapper.UserMapper;
import com.cjx.pojo.User;
import com.cjx.service.UserService;
import com.cjx.utils.Md5Util;
import com.cjx.utils.ThreadLocalUtil;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类，提供用户相关的操作
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 返回匹配的User对象，如果没有找到返回null
     */
    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码（明文）
     *  注：密码会被加密后存储
     */
    @Override
    public void register(String username, String password) {
        String md5String = Md5Util.getMD5String(password);
        userMapper.add(username, md5String);
    }

    /**
     * 验证密码是否正确
     * @param password 用户输入的密码（明文）
     * @param password1 存储的加密密码
     * @return 如果密码匹配返回true，否则返回false
     */
    @Override
    public boolean checkPassword(String password, String password1) {
        return Md5Util.checkPassword(password, password1);
    }

    /**
     * 更新用户信息
     * @param user 包含更新信息的User对象
     */
    @Override
    public void update(User user) {
        // 更新用户信息前设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    /**
     * 更新用户头像
     * @param avatarUrl 头像的URL
     * 注：该操作需要当前用户ID，通过ThreadLocal获取
     */
    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        userMapper.updateAvatar(avatarUrl, id);
    }

    /**
     * 更新用户密码
     * @param newPwd 新密码（明文）
     * 注：密码会被加密后存储
     */
    @Override
    public void updatePassword(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer)map.get("id");
        userMapper.updatePassword(Md5Util.getMD5String(newPwd), id);
    }
}
