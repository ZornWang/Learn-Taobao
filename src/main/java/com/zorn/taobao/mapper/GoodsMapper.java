package com.zorn.taobao.mapper;

import com.zorn.taobao.pojo.Goods;
import com.zorn.taobao.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface GoodsMapper {
    /**
     * 查询某个用户的所有商品
     * @param user
     * @return
     */
    User userGoods(User user);

    void addGoods(Goods goods);

    void addUserGoods(Integer userId,Integer goodsId,Integer number);

    @Select("select * from goods where id = #{goodsId}")
    Goods getGoodsById(Integer goodsId);

    void changeGoodsOwner(Integer userId, Integer goodsId);

    void changeGoods(Goods goods);

    void completeGoods(Goods goods);

    @Delete("delete from goods where id = #{goodsId}")
    void deleteGoods(Integer goodsId);

    @Delete("delete from user_goods where goods_id = #{goodsId}")
    void deleteUserGoods(Integer goodsId);

    @Update("alter table goods auto_increment = 1")
    void setGoodsAutoIncrement();

    @Update("alter table user_goods auto_increment = 1")
    void setUserGoodsAutoIncrement();
}

