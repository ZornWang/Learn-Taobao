package com.zorn.taobao.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "User",description = "用户")
public class User {
    @ApiModelProperty(value = "用户id")

    private int id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @JsonBackReference
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "头像Url")
    private String avatarUrl;

    private List<Goods> goodsList;

    public User() {
    }

    public User(int id, String username, String phone) {
        this.id = id;
        this.username = username;
        this.phone = phone;
    }

    public User(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public User(int id, String username, String phone, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public User(int id, String username, String password, String phone, String avatarUrl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
