package com.zorn.taobao.service.impl;

import com.zorn.taobao.mapper.GoodsMapper;
import com.zorn.taobao.mapper.UserMapper;
import com.zorn.taobao.pojo.Goods;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.pojo.state.COSUrl;
import com.zorn.taobao.pojo.state.ResponseState;
import com.zorn.taobao.service.GoodsService;
import com.zorn.taobao.utils.TencentCOSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public ResponseData addGoods(Goods goods, User user, Integer number) {
        if (goods == null) {
            return new ResponseData(ResponseState.GOODS_ERROR.getMessage(), ResponseState.GOODS_ERROR.getValue());
        }
        try {
            goodsMapper.addGoods(goods);
            User userByName = userMapper.getUserByName(user.getUsername());
            goodsMapper.addUserGoods(userByName.getId(), goods.getId(), number);
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    @Transactional
    public ResponseData buyGoods(Goods goods, User user) {
        Goods goodsById = goodsMapper.getGoodsById(goods.getId());
        if (goodsById == null) {
            return new ResponseData(ResponseState.GOODS_NOT_EXIST.getMessage(), ResponseState.GOODS_NOT_EXIST.getValue());
        }
        try {
            goodsMapper.changeGoodsOwner(user.getId(), goodsById.getId());
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    @Transactional
    public ResponseData changeGoods(Goods goods) {
        Goods goodsById = goodsMapper.getGoodsById(goods.getId());
        if (goodsById == null) {
            return new ResponseData(ResponseState.GOODS_NOT_EXIST.getMessage(), ResponseState.GOODS_NOT_EXIST.getValue());
        }
        try {
            goodsMapper.changeGoods(goods);
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    @Transactional
    public ResponseData completeGoods(Goods goods) {
        Goods goodsById = goodsMapper.getGoodsById(goods.getId());
        if (goodsById == null) {
            return new ResponseData(ResponseState.GOODS_NOT_EXIST.getMessage(), ResponseState.GOODS_NOT_EXIST.getValue());
        }
        try {
            goodsMapper.completeGoods(goods);
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    @Transactional
    public ResponseData deleteGoods(Integer goodsId) {
        Goods goodsById = goodsMapper.getGoodsById(goodsId);
        if (goodsById == null) {
            return new ResponseData(ResponseState.GOODS_NOT_EXIST.getMessage(), ResponseState.GOODS_NOT_EXIST.getValue());
        }
        try {
            goodsMapper.deleteUserGoods(goodsId);
            goodsMapper.deleteGoods(goodsId);
            goodsMapper.setGoodsAutoIncrement();
            goodsMapper.setUserGoodsAutoIncrement();
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    public ResponseData getUserGoods(User user) {
        User userGoods;
        try {
            User userByName = userMapper.getUserByName(user.getUsername());
            userGoods = goodsMapper.userGoods(userByName);
            if (userGoods.getGoodsList() == null) {
                return new ResponseData(ResponseState.USERSGOODS_NOT_EXIST.getMessage(), ResponseState.USERSGOODS_NOT_EXIST.getValue());
            }
            return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue(), "GoodsList", userGoods.getGoodsList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }

    @Override
    public ResponseData addGoodsAvatar(String goodsName, MultipartFile file) {
        if (file == null) {
            return new ResponseData(ResponseState.UPLOAD_IS_NULL.getMessage(), ResponseState.UPLOAD_IS_NULL.getValue());
        }
        try {
            String fileName = goodsName + "_picture_" + System.currentTimeMillis();
            String uploadfile = TencentCOSUtil.uploadfile(file, COSUrl.USERAVATAR.getUrl(), fileName);
            return new ResponseData(uploadfile, ResponseState.SUCCESS.getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(ResponseState.ERROR.getMessage(), ResponseState.ERROR.getValue());
        }
    }
}
