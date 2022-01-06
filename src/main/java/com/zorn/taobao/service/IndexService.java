package com.zorn.taobao.service;

import com.zorn.taobao.pojo.ResponseData;

public interface IndexService {
    ResponseData getAllGoods();

    ResponseData searchGoods(String goodsName);
}
