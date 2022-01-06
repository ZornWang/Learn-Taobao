package com.zorn.taobao.pojo.state;

public enum RedisHeader {
    USER_TOKEN("userToken"),
    USER_REFRESH_TOKEN("userRefreshToken"),
    RETRIEVE_PASSWORD_CODE("retrievePasswordCode"),
    REGISTER_CODE("registerCode");

    private String header;

    RedisHeader(String header) {
        this.header = "taobao" + header;
    }

    public String getHeader() {
        return header;
    }
}
