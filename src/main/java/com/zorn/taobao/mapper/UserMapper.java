package com.zorn.taobao.mapper;

import com.zorn.taobao.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    @Select("select * from user where username=#{username};")
    User getUserByName(@Param("username") String username);

    @Select("select * from user where phone=#{phone};")
    User getUserByPhone(@Param("phone") String phone);

    @Insert("insert into user (username,password,phone) values (#{username},#{password},#{phone})")
    void insertUser(User user);

    @Update("update user set password=#{password} where phone=#{phone}")
    void changePassword(User user);

    @Update("update user set avatar_url=#{avatarUrl} where phone=#{phone}")
    void saveAvatar(@Param("phone") String phone, @Param("avatarUrl") String avatarUrl);
}
