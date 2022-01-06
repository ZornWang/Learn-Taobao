package com.zorn.taobao.service;

import com.zorn.taobao.pojo.Goods;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import org.springframework.web.multipart.MultipartFile;

public interface GoodsService {
    ResponseData addGoods(Goods goods, User user, Integer number);

    ResponseData buyGoods(Goods goods, User user);

    ResponseData changeGoods(Goods goods);

    ResponseData completeGoods(Goods goods);

    ResponseData deleteGoods(Integer goodsId);

    ResponseData getUserGoods(User user);

    ResponseData addGoodsAvatar(String goodsName, MultipartFile file);
}
