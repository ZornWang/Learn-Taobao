package com.zorn.taobao.pojo.state;

public enum COSUrl {
    USERAVATAR("/userAvatar/");
    private String url;

    COSUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
