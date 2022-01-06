package com.zorn.taobao.controller;

import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "主页")
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    IndexService indexService;

    @ApiOperation("获取所有商品")
    @GetMapping("/getAllGoods")
    public ResponseData getAllGoods() {
        return indexService.getAllGoods();
    }

    @ApiOperation("关键词搜索商品")
    @ApiImplicitParam(name = "goodsName", value = "goodsName", required = true, paramType = "query")
    @GetMapping("/searchGoods")
    public ResponseData searchGoods(String goodsName) {
        return indexService.searchGoods(goodsName);
    }
}
