package com.zorn.taobao.interceptor;

import com.alibaba.fastjson.JSON;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.pojo.state.RedisHeader;
import com.zorn.taobao.pojo.state.ResponseState;
import com.zorn.taobao.utils.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("拦截器工作");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        ResponseData responseData = new ResponseData();
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            //未传入token
            responseData.setMessageState("未传入token", ResponseState.TOKEN_NOT_PROVIDE.getValue());//101
            String json = JSON.toJSONString(responseData);
            response.getWriter().append(json);
            return false;
        }

        String[] authorizations = authorization.split(" ");
        //判断是否为Bearer类型
        if (!authorizations[0].equals("Bearer")) {
            //token错误，不是Bearer类型
            responseData = new ResponseData("token错误", ResponseState.TOKEN_IS_ERROR.getValue());
            String json = JSON.toJSONString(responseData);
            response.getWriter().append(json);
            return false;
        }

        String token = authorizations[1];
        //判断是否传入token
        if (token == null) {
            //未传入token
            responseData.setMessageState("未传入token",ResponseState.TOKEN_NOT_PROVIDE.getValue());//101
            String json = JSON.toJSONString(responseData);
            response.getWriter().append(json);
            return false;
        }

        try {
            User user = null;
            try {
                user = JWTUtil.getUser(token, request);
            } catch (Exception e) {
                //token错误，不能正常解析
                e.printStackTrace();
                responseData.setMessageState("token错误", ResponseState.TOKEN_NOT_PROVIDE.getValue());//101
                String json = JSON.toJSONString(responseData);
                response.getWriter().append(json);
                return false;
            }
            if (user == null) {
                //解析后user为空
                responseData.setMessageState("系统错误", ResponseState.ERROR.getValue());//100
                String json = JSON.toJSONString(responseData);
                response.getWriter().append(json);
                return false;
            }
            //成功解析

            String rightToken = this.stringRedisTemplate.opsForValue().get(RedisHeader.USER_TOKEN.getHeader() + user.getPhone());
            if (token.equals(rightToken)) {
                return true;
            } else {
                responseData=new ResponseData("token错误",ResponseState.TOKEN_IS_ERROR.getValue());
                String Json= JSON.toJSONString(responseData);
                response.getWriter().append(Json);
                return false;
            }

        } catch (ExpiredJwtException e) {
            //token过期
            responseData.setMessageState("token已过期", ResponseState.TOKEN_IS_EXPIRED.getValue());//103
            String Json = JSON.toJSONString(responseData);
            response.getWriter().append(Json);
            return false;
        } catch (Exception e) {
            //发生错误
            responseData.setMessageState("系统错误", ResponseState.ERROR.getValue());//100
            String json = JSON.toJSONString(responseData);
            response.getWriter().append(json);
            return false;
        }
    }
}
