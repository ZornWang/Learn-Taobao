package com.zorn.taobao.controller;

import com.zorn.taobao.annotation.ApiJsonObject;
import com.zorn.taobao.annotation.ApiJsonProperty;
import com.zorn.taobao.pojo.Goods;
import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    /**
     * 查询某用户拥有的所有商品
     * @param user user
     * @return ResponseData
     */
    @ApiOperation("获取当前用户的所有商品")
    @GetMapping("/getUserGoods")
    public ResponseData getUserGoods(@ApiIgnore @ModelAttribute User user) {
        return goodsService.getUserGoods(user);
    }

    /**
     * 当前用户添加商品
     * @param user user
     * @param param 请求参数，包含商品信息
     * @return ResponseData
     */
    @ApiOperation("当前用户添加商品")
    @PostMapping("/addGoods")
    public ResponseData addGoods(@ApiIgnore @ModelAttribute User user,
                                 @ApiJsonObject(name = "addGoods", value = {
                                         @ApiJsonProperty(key = "pictureUrl", description = "商品图片"),
                                         @ApiJsonProperty(key = "name", description = "商品名称"),
                                         @ApiJsonProperty(key = "type", description = "商品类别"),
                                         @ApiJsonProperty(key = "price", description = "商品价格"),
                                         @ApiJsonProperty(key = "introduction", description = "商品介绍"),
                                         @ApiJsonProperty(key = "number", description = "商品数量")
                                 })@RequestBody Map<String, Object> param) {
        return goodsService.addGoods(new Goods((String) param.get("pictureUrl"), (String) param.get("name"),
                (String) param.get("type"), (String)param.get("price"), (String) param.get("introduction")), user, (int) param.get("number"));
    }

    /**
     * 上传商品图片
     * @param goodsName 商品名称
     * @param file 商品图片
     * @return ResponseData
     */
    @ApiOperation("上传商品图片")
    @PostMapping("/uploadGoodsPicture")
    public ResponseData upload(@ApiParam(value = "商品名称", name = "goodsName", required = true) @RequestParam String goodsName,
                               @ApiParam(value = "商品图片", name = "file", required = true) @RequestParam MultipartFile file) {
        return goodsService.addGoodsAvatar(goodsName, file);
    }

    /**
     * 购买商品
     * @param id 商品id
     * @param name 商品名称
     * @param user user
     * @return ResponseData
     */
    @ApiOperation("购买商品")
    @PostMapping("/buyGoods")
    public ResponseData buyGoods(@ApiParam(value = "商品id", name = "id", required = true) @RequestParam Integer id,
                                 @ApiParam(value = "商品名称", name = "name", required = true) @RequestParam String name,
                                 @ApiIgnore @ModelAttribute User user) {
        return goodsService.buyGoods(new Goods(id,name), user);
    }

    /**
     * 修改商品信息
     *
     * @param param 请求参数，包含商品新信息
     * @return ResponseData
     */
    @ApiOperation("修改商品信息")
    @PutMapping("/changeGoods")
    public ResponseData changeGoods(@ApiJsonObject(name = "changeGoods", value = {
            @ApiJsonProperty(key = "id", description = "商品id"),
            @ApiJsonProperty(key = "pictureUrl", description = "商品图片"),
            @ApiJsonProperty(key = "name", description = "商品名称"),
            @ApiJsonProperty(key = "type", description = "商品类别"),
            @ApiJsonProperty(key = "price", description = "商品价格"),
            @ApiJsonProperty(key = "introduction", description = "商品介绍")
    }) @RequestBody Map<String, Object> param) {
        return goodsService.changeGoods(new Goods((int) param.get("id"), (String) param.get("pictureUrl"), (String) param.get("name"), (String) param.get("type"),
                (String) param.get("price"), (String) param.get("introduction")));
    }

    @ApiOperation("完善商品信息")
    @PutMapping("/completeGoods")
    public ResponseData completeGoods(@ApiJsonObject(name = "completeGoods", value = {
            @ApiJsonProperty(key = "id", description = "商品id"),
            @ApiJsonProperty(key = "pictureUrl", description = "商品图片"),
            @ApiJsonProperty(key = "name", description = "商品名称"),
            @ApiJsonProperty(key = "type", description = "商品类别"),
            @ApiJsonProperty(key = "price", description = "商品价格"),
            @ApiJsonProperty(key = "introduction", description = "商品介绍")
    }) @RequestBody Map<String, Object> param) {
        return goodsService.completeGoods(new Goods((int) param.get("id"), (String) param.get("pictureUrl"), (String) param.get("name"), (String) param.get("type"),
                (String) param.get("price"), (String) param.get("introduction")));
    }

    /**
     * 删除商品
     * @param goodsId 商品id
     * @return ResponseData
     */
    @ApiOperation("删除商品")
    @DeleteMapping("/deleteGoods")
    public ResponseData deleteGoods(@ApiParam(value = "商品id", name = "id", required = true) @RequestParam Integer goodsId) {
        return goodsService.deleteGoods(goodsId);
    }
}
