package com.zorn.taobao.pojo.state;

public enum TemplateId {
    REGISTER("1061367"),//注册验证码
    FORGET_PASSWORD("1075868");//找回密码验证码


    private String value;

    TemplateId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
