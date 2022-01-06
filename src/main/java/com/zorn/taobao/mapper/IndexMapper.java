package com.zorn.taobao.mapper;

import com.zorn.taobao.pojo.Goods;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface IndexMapper {

    @Select("select * from goods")
    Goods[] getAllGoods();

    @Select("select * from goods where name like \"%\"#{goodsName}\"%\"")
    Goods[] searchGoodsByName(String goodsName);
}
