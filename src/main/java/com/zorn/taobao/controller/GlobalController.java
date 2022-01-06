package com.zorn.taobao.controller;

import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.pojo.state.ResponseState;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalController {

    /**
     * 处理请求参数格式错误的返回信息
     *
     * @param e：BindException异常
     * @return responseData
     */
    @ExceptionHandler
    public ResponseData BindExceptionHandler(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return new ResponseData(message, ResponseState.PARAM_IS_ERROR.getValue());
    }

    /**
     * 获取User对象
     *
     * @param data：储存user对象
     * @param request：HttpServletRequest
     */
    @ModelAttribute
    public void getModel(Map<String, Object> data, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        data.put("user", user);
    }
}
