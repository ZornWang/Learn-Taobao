package com.zorn.taobao.pojo.state;

public enum ResponseState {
    SUCCESS("操作成功", 200),
    TOKEN_NOT_PROVIDE("未传入token", 101),
    TOKEN_IS_ERROR("token错误", 102),
    TOKEN_IS_EXPIRED("token已过期", 103),
    REFRESH_TOKEN_IS_ERROR("refreshToken错误", 104),
    REFRESH_TOKEN_IS_EXPIRED("refreshToken已过期", 105),
    USER_NOT_EXIST("用户不存在", 106),
    USER_IS_EXIST("用户已存在", 107),
    PASSWORD_IS_ERROR("密码错误", 108),
    CODE_NOT_EXIST("验证码未获取或已过期", 109),
    CODE_IS_ERROR("验证码错误", 110),
    PARAM_IS_ERROR("参数错误", 111),
    UPLOAD_IS_NULL("上传文件为空", 112),
    USERSGOODS_NOT_EXIST("用户未拥有商品",113),
    GOODS_NOT_EXIST("商品不存在",114),
    GOODS_ERROR("商品信息错误", 115),
    ERROR("操作失败", 100);

    private String message;
    private int value;

    ResponseState(String message, int value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }
}
