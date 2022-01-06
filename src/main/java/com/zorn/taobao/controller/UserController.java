package com.zorn.taobao.controller;

import com.zorn.taobao.annotation.ApiJsonObject;
import com.zorn.taobao.annotation.ApiJsonProperty;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    /**
     * 用户登录
     *
     * @param param：请求参数，包含用户名和密码
     * @return user信息，token
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseData login(@ApiJsonObject(name = "Login", value = {
            @ApiJsonProperty(key = "username", description = "用户名"),
            @ApiJsonProperty(key = "password", description = "密码")
    })@RequestBody Map<String, Object> param) {
        return userService.login((String) param.get("username"), (String) param.get("password"));
    }

    /**
     * 用户注册
     *
     * @param param：请求参数，包含用户名、密码和手机号
     * @return ResponseData
     */
    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseData register(@ApiJsonObject(name = "Register", value = {
            @ApiJsonProperty(key = "username", description = "用户名"),
            @ApiJsonProperty(key = "password", description = "密码"),
            @ApiJsonProperty(key = "phone", description = "手机号"),
            @ApiJsonProperty(key = "code", description = "验证码")
    })@RequestBody Map<String, String> param) {

        return userService.register(new User(param.get("username"), param.get("password"), param.get("phone")), param.get("code"));
    }

    /**
     * 用户注册获取验证码
     *
     * @param phone：手机号
     * @return ResponseData
     */
    @ApiOperation("获取注册验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @GetMapping("/register/getCode")
    public ResponseData getCode(String phone) {
        return userService.getRegistCode(phone);
    }

    /**
     * 找回密码
     *
     * @param param：包含用户名，要设置的新密码、手机号和验证码
     * @return ResponseData
     */
    @ApiOperation("找回密码")
    @PostMapping("/forget")
    public ResponseData forgetPassword(@ApiJsonObject(name = "ForgetPassword", value = {
            @ApiJsonProperty(key = "username", description = "用户名"),
            @ApiJsonProperty(key = "password", description = "新密码"),
            @ApiJsonProperty(key = "phone", description = "手机号"),
            @ApiJsonProperty(key = "code", description = "验证码")
    }) @RequestBody Map<String, String> param) {
        return userService.forgetPassword(new User(param.get("username"), param.get("password"), param.get("phone")), param.get("code"));
    }

    /**
     * 获取找回密码验证码
     *
     * @param phone：手机号
     * @return ResponseData
     */
    @ApiOperation("获取找回验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @GetMapping("/forget/getCode")
    public ResponseData getForgetCode(String phone) {
        return userService.getForgetCode(phone);
    }

    /**
     * 退出登录
     *
     * @param user：当前登录的用户信息，包含手机号即可
     * @return ResponseData
     */
    @ApiOperation("退出登录")
    @DeleteMapping
    public ResponseData logout(@ApiIgnore @ModelAttribute("user") User user) {
        return userService.logout(user);
    }

    /**
     * 获取refreshToken
     *
     * @param refreshToken：refreshToken
     * @return token，refreshToken
     */
    @ApiOperation("刷新Token")
    @ApiImplicitParam(name = "refreshToken", value = "refreshToken", required = true, paramType = "query")
    @GetMapping("/refresh")
    public ResponseData refreshToken(String refreshToken) {
        return userService.refreshToken(refreshToken);
    }

    /**
     * 获取当前登录的用户信息
     *
     * @param user：包含用户名即可
     * @param phone：手机号，可为空，为空查询自动的信息
     * @return ResponseData
     */
    @ApiOperation("获取当前登录的用户信息")
    @ApiImplicitParam(name = "phone", value = "手机号", required = false, paramType = "query")
    @GetMapping
    public ResponseData getUser(String phone, @ApiIgnore @ModelAttribute("user") User user) {
        return userService.getUser(user, phone);
    }

    /**
     * 修改密码
     *
     * @param param：包含newPassword和oldPassword
     * @param user：user
     * @return ResponseData
     */
    @ApiOperation("修改密码")
    @PutMapping("/changePassword")
    public ResponseData changePassword(@ApiJsonObject(name = "UserChangePassword", value = {
            @ApiJsonProperty(key = "newPassword", description = "新密码"),
            @ApiJsonProperty(key = "oldPassword", description = "旧密码")
    }) @RequestBody Map<String, String> param, @ApiIgnore @ModelAttribute("user") User user) {
        return userService.changePassword(user, param.get("newPassword"), param.get("oldPassword"));
    }

    /**
     * 上传头像
     *
     * @param file：用户头像
     * @param user：user
     * @return ResponseData
     */
    @ApiOperation("上传头像")
    @PostMapping("/uploadAvatar")
    public ResponseData upload(@ApiParam(value = "用户头像", name = "file", required = true) @RequestParam(value = "file") MultipartFile file,
                               @ApiIgnore @ModelAttribute("user") User user) {
        return userService.uploadAvatar(user.getUsername(), file);
    }

    /**
     * 保存头像
     *
     * @param param：包含头像地址url
     * @param user：user
     * @return ResponseData
     */
    @ApiOperation("保存头像（需要提供avatarUrl）")
    @PostMapping("/uploadAvatar/save")
    public ResponseData save(@ApiJsonObject(name = "SaveUserAvatar", value = {
            @ApiJsonProperty(key = "avatarUrl", description = "用户头像Url")
    }) @RequestBody Map<String, String> param, @ApiIgnore @ModelAttribute("user") User user) {
        return userService.save(param.get("avatarUrl"), user.getPhone());
    }


}
