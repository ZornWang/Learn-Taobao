package com.zorn.taobao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.pojo.state.COSUrl;
import com.zorn.taobao.pojo.state.RedisHeader;
import com.zorn.taobao.pojo.state.ResponseState;
import com.zorn.taobao.pojo.state.TemplateId;
import com.zorn.taobao.mapper.UserMapper;
import com.zorn.taobao.service.UserService;
import com.zorn.taobao.utils.JWTUtil;
import com.zorn.taobao.utils.TencentCOSUtil;
import com.zorn.taobao.utils.TencentSMSUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserMapper userMapper;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ResponseData login(String username, String password) {
        User user = userMapper.getUserByName(username);
        if (user == null) {
            return new ResponseData(ResponseState.USER_NOT_EXIST.getMessage(), ResponseState.USER_NOT_EXIST.getValue());
        }
        if (!passwordEncoder.matches(password,user.getPassword())
        /*!password.equals(user.getPassword())*/) {
            return new ResponseData(ResponseState.PASSWORD_IS_ERROR.getMessage(), ResponseState.PASSWORD_IS_ERROR.getValue());
        }

        try {
            User loginuser = new User(user.getUsername(), user.getPassword(), user.getPhone());

            //token
            String token = JWTUtil.getToken(loginuser);
            String phone = user.getPhone();
            stringRedisTemplate.opsForValue().set(RedisHeader.USER_TOKEN.getHeader() + phone, token);
            stringRedisTemplate.expire(RedisHeader.USER_TOKEN.getHeader() + phone,1,TimeUnit.HOURS);
            //refreshToken
            String refreshToken = JWTUtil.getRefreshToken(loginuser);
            stringRedisTemplate.opsForValue().set(RedisHeader.USER_REFRESH_TOKEN.getHeader()+phone,refreshToken);
            stringRedisTemplate.expire(RedisHeader.USER_REFRESH_TOKEN.getHeader()+phone,10,TimeUnit.DAYS);

            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue(), token, refreshToken, "user", user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 退出登录
     * @param user
     * @return
     */
    @Override
    public ResponseData logout(User user) {
        try {
            stringRedisTemplate.delete(RedisHeader.USER_TOKEN.getHeader() + user.getPhone());
            stringRedisTemplate.delete(RedisHeader.USER_REFRESH_TOKEN.getHeader() + user.getPhone());
            stringRedisTemplate.delete(RedisHeader.REGISTER_CODE.getHeader() + user.getPhone());
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 获取refreshToken
     * @param refreshToken
     * @return
     */
    @Override
    public ResponseData refreshToken(String refreshToken) {
        try {
            User user = null;
            try {
                user = JWTUtil.getUser(refreshToken);
            } catch (ExpiredJwtException e) {
                return new ResponseData(ResponseState.REFRESH_TOKEN_IS_EXPIRED.getMessage(), ResponseState.REFRESH_TOKEN_IS_EXPIRED.getValue());
            } catch (Exception e) {
                return new ResponseData(ResponseState.REFRESH_TOKEN_IS_ERROR.getMessage(), ResponseState.REFRESH_TOKEN_IS_ERROR.getValue());
            }

            stringRedisTemplate.delete(RedisHeader.USER_TOKEN.getHeader() + user.getPhone());
            stringRedisTemplate.delete(RedisHeader.USER_REFRESH_TOKEN.getHeader() + user.getPhone());

            String newToken = JWTUtil.getToken(user);
            String phone = user.getPhone();
            stringRedisTemplate.opsForValue().set(RedisHeader.USER_TOKEN.getHeader() + phone, newToken);
            stringRedisTemplate.expire(RedisHeader.USER_TOKEN.getHeader() + phone,1,TimeUnit.HOURS);

            String newRefreshToken = JWTUtil.getRefreshToken(user);
            stringRedisTemplate.opsForValue().set(RedisHeader.USER_REFRESH_TOKEN.getHeader() + phone, newRefreshToken);
            stringRedisTemplate.expire(RedisHeader.USER_REFRESH_TOKEN.getHeader()+phone,10,TimeUnit.DAYS);

            ResponseData responseData = new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
            responseData.setTokens(newToken, newRefreshToken);
            return responseData;

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.REFRESH_TOKEN_IS_ERROR.getMessage(), ResponseState.REFRESH_TOKEN_IS_ERROR.getValue());
        }
    }

    /**
     * 获取用户信息
     * @param user
     * @param phone
     * @return
     */
    @Override
    public ResponseData getUser(User user, String phone) {
        if (phone == null) {
            phone = user.getPhone();
        }
        try {
            User userByPhone = userMapper.getUserByPhone(phone);
            if (userByPhone == null) {
                return new ResponseData(ResponseState.USER_NOT_EXIST.getMessage(), ResponseState.USER_NOT_EXIST.getValue());//104
            }
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue(), "user", userByPhone);
        } catch (Exception e) {
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 获取注册验证码
     * @param phone
     * @return
     */
    @Override
    public ResponseData getRegistCode(String phone) {
        try {
            if (phone == null) {
                return new ResponseData("手机号为空", ResponseState.ERROR.getValue());
            }
            //获取验证码
//            String code = "111111";
            String code = getCode();
            String templateId = TemplateId.REGISTER.getValue();

            SendSmsResponse rep = TencentSMSUtil.sendSMS(phone, code, templateId);

//            System.out.println(SendSmsResponse.toJsonString(rep));

            JSONObject jsonObject = JSON.parseObject(SendSmsResponse.toJsonString(rep));
            String code1 = jsonObject.getString("SendStatusSet");
            JSONObject object = JSON.parseObject(code1.substring(1,code1.length()-1));
            String backcode = object.getString("Code");

            if (backcode.equals("Ok")) {
                stringRedisTemplate.opsForValue().set(RedisHeader.REGISTER_CODE.getHeader() + phone, code);
                stringRedisTemplate.expire(RedisHeader.REGISTER_CODE.getHeader() + phone, 5, TimeUnit.MINUTES);
                return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
            } else {
                return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
            }


        } catch (Exception e) {
            //获取失败
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 随机生成验证码
     * @return
     */
    @Override
    public String getCode() {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(code);
    }

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    @Override
    public ResponseData register(User user, String code) {
        try {
            String rightCode = stringRedisTemplate.opsForValue().get(RedisHeader.REGISTER_CODE.getHeader() + user.getPhone());
            if (rightCode == null) {
                //为获取验证码或已过期
                return new ResponseData(ResponseState.CODE_NOT_EXIST.getMessage(), ResponseState.CODE_NOT_EXIST.getValue());//107
            }
            if (code == null || !code.equals(rightCode)) {
                //验证码输入错误
                return new ResponseData("验证码错误", ResponseState.CODE_IS_ERROR.getValue());
            }
            User userByPhone = userMapper.getUserByPhone(user.getPhone());
            if (userByPhone != null) {
                //用户已存在
                return new ResponseData("该用户已存在", ResponseState.USER_IS_EXIST.getValue());//106
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insertUser(user);
            stringRedisTemplate.delete(RedisHeader.REGISTER_CODE.getHeader() + user.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData("系统错误", ResponseState.ERROR.getValue());
        }
        return new ResponseData("注册成功", ResponseState.SUCCESS.getValue());
    }

    /**
     * 忘记密码
     * @param user
     * @param code
     * @return
     */
    @Override
    public ResponseData forgetPassword(User user, String code) {
        try {
            String rightCode = stringRedisTemplate.opsForValue().get(RedisHeader.RETRIEVE_PASSWORD_CODE.getHeader() + user.getPhone());
            if (rightCode == null) {
                //为获取验证码或已过期
                return new ResponseData(ResponseState.CODE_NOT_EXIST.getMessage(), ResponseState.CODE_NOT_EXIST.getValue());//107
            }
            if (code == null || !code.equals(rightCode)) {
                //验证码输入错误
                return new ResponseData("验证码错误", ResponseState.CODE_IS_ERROR.getValue());
            }
            User userByPhone = userMapper.getUserByPhone(user.getPhone());
            if (userByPhone == null) {
                //用户不存在
                return new ResponseData("该用户不存在", ResponseState.USER_NOT_EXIST.getValue());//106
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.changePassword(user);
            stringRedisTemplate.delete(RedisHeader.RETRIEVE_PASSWORD_CODE.getHeader() + user.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData("系统错误", ResponseState.ERROR.getValue());
        }
        return new ResponseData("找回密码成功", ResponseState.SUCCESS.getValue());
    }

    /**
     * 获取忘记密码验证码
     * @param phone
     * @return
     */
    @Override
    public ResponseData getForgetCode(String phone) {
        try {
            //获取验证码
//            String code = "111111";
            String code = getCode();
            String templateId = TemplateId.FORGET_PASSWORD.getValue();

            SendSmsResponse rep = TencentSMSUtil.sendSMS(phone, code, templateId);

//            System.out.println(SendSmsResponse.toJsonString(rep));

            JSONObject jsonObject = JSON.parseObject(SendSmsResponse.toJsonString(rep));
            String code1 = jsonObject.getString("SendStatusSet");
            JSONObject object = JSON.parseObject(code1.substring(1,code1.length()-1));
            String backcode = object.getString("Code");

            if (backcode.equals("Ok")) {
                stringRedisTemplate.opsForValue().set(RedisHeader.RETRIEVE_PASSWORD_CODE.getHeader() + phone, code);
                stringRedisTemplate.expire(RedisHeader.RETRIEVE_PASSWORD_CODE.getHeader() + phone, 5, TimeUnit.MINUTES);
                return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
            } else {
                return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
            }


        } catch (Exception e) {
            //获取失败
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 修改密码
     * @param user
     * @param newPassword
     * @param oldPassword
     * @return
     */
    @Override
    public ResponseData changePassword(User user, String newPassword, String oldPassword) {
        try {
            User userByPhone = userMapper.getUserByPhone(user.getPhone());
            if (userByPhone == null) {
                //用户不存在
                return new ResponseData(ResponseState.USER_NOT_EXIST.getMessage(), ResponseState.USER_NOT_EXIST.getValue());
            }
            if (!passwordEncoder.matches(oldPassword, userByPhone.getPassword())) {
                //原密码错误
                return new ResponseData(ResponseState.PASSWORD_IS_ERROR.getMessage(), ResponseState.PASSWORD_IS_ERROR.getValue());
            }
            userMapper.changePassword(new User(user.getUsername(), passwordEncoder.encode(newPassword), user.getPhone()));
            //修改成功
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            //修改失败
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 上传头像
     * @param username
     * @param file
     * @return
     */
    @Override
    public ResponseData uploadAvatar(String username, MultipartFile file) {
        if (file == null) {
            return new ResponseData("上传文件为空", ResponseState.ERROR.getValue());
        }
        try {
            String fileName = username + "_avatar_" + System.currentTimeMillis();
            String uploadfile = TencentCOSUtil.uploadfile(file, COSUrl.USERAVATAR.getUrl(), fileName);
            return new ResponseData(uploadfile, ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    /**
     * 存储头像Url
     * @param avatarUrl
     * @param phone
     * @return
     */
    @Override
    public ResponseData save(String avatarUrl, String phone) {
        if (avatarUrl == null) {
            return new ResponseData("Url为空", ResponseState.ERROR.getValue());
        }
        try {
            userMapper.saveAvatar(phone, avatarUrl);
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }
}