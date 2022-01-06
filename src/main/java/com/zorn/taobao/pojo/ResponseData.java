package com.zorn.taobao.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "返回类")
public class ResponseData {
    @ApiModelProperty("状态信息")
    private String message;
    @ApiModelProperty("状态码")
    private int state;
    @ApiModelProperty("accessToken")
    private String token;
    @ApiModelProperty("refreshToken")
    private String refreshToken;
    @ApiModelProperty("返回数据")
    private Map<String, Object> data = new HashMap<>();

    public ResponseData(String message, int state, String token, String refreshToken, String key, Object value) {
        this.message = message;
        this.state = state;
        this.token = token;
        this.refreshToken = refreshToken;
        this.data.put(key, value);
    }

    public ResponseData() {
    }

    public ResponseData(String message, int state) {
        this.message = message;
        this.state = state;
    }

    public ResponseData(String token, String refreshToken, String key, Object value) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.data.put(key, value);
    }

    public ResponseData(String message, int state, String key, Object value) {
        this.message = message;
        this.state = state;
        this.data.put(key, value);
    }

    public void setMessageState(String message, int state) {
        this.message = message;
        this.state = state;
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getData(String key) {
        return this.data.get(key);
    }

    public void setTokens(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}