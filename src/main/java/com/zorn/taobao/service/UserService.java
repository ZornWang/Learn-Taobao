package com.zorn.taobao.service;

import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseData login(String username, String password);

    ResponseData logout(User user);

    ResponseData refreshToken(String refreshToken);

    ResponseData getUser(User user, String phone);

    ResponseData getRegistCode(String phone);

    String getCode();

    ResponseData forgetPassword(User user, String code);

    ResponseData getForgetCode(String phone);

    ResponseData register(User user, String code);

    ResponseData changePassword(User user,String newPassword,String oldPassword);

    ResponseData uploadAvatar(String username, MultipartFile file);

    ResponseData save(String avatarUrl, String phone);

}
