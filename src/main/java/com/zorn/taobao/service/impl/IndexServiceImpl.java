package com.zorn.taobao.service.impl;

import com.zorn.taobao.mapper.IndexMapper;
import com.zorn.taobao.pojo.Goods;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.state.ResponseState;
import com.zorn.taobao.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    IndexMapper indexMapper;

    @Override
    public ResponseData getAllGoods() {
        try {
            Goods[] allGoods = indexMapper.getAllGoods();
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue(), "allGoods", allGoods);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    public ResponseData searchGoods(String goodsName) {
        try {
            Goods[] allGoods = indexMapper.searchGoodsByName(goodsName);
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue(), "resultGoods", allGoods);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }
}
