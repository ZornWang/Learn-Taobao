package com.zorn.taobao.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Goods",description = "商品")
public class Goods {

    @ApiModelProperty(value = "商品id")
    private int id;
    @ApiModelProperty(value = "商品图片Url")
    private String pictureUrl;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "商品类型")
    private String type;
    @ApiModelProperty(value = "商品价格")
    private String price;
    @ApiModelProperty(value = "商品介绍")
    private String introduction;

    public Goods(String pictureUrl, String name, String type, String price, String introduction) {
        this.pictureUrl = pictureUrl;
        this.name = name;
        this.type = type;
        this.price = price;
        this.introduction = introduction;
    }

    public Goods(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
