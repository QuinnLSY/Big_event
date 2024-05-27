package com.cjx.mapper;

import com.cjx.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户
    @Select({"select * from user where username=#{username}"})
    User findByUserName(String username);
    // 添加用户
    @Insert({"insert into user(username,password) values(#{username},#{password})"})
    void add(String username, String password);
    // 更新用户信息
    @Update({"update user set nickname=#{nickname},email=#{email},user_pic=#{userPic}, update_time=#{updateTime} where id=#{id}"})
    void update(User user);
    // 更新用户头像
    @Update({"update user set user_pic=#{avatarUrl}, update_time=now() where id=#{id}"})
    void updateAvatar(String avatarUrl, Integer id);
    // 更新用户密码
    @Update({"update user set password=#{md5PwdStr}, update_time=now() where id=#{id}"})
    void updatePassword(String md5PwdStr, Integer id);
}

