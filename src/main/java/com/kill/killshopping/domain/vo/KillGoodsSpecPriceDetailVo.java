package com.kill.killshopping.domain.vo;

import com.kill.killshopping.domain.entity.KillGoodsPrice;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Date 2019/12/12 16:43
 */
@Data
public class KillGoodsSpecPriceDetailVo extends KillGoodsPrice implements Serializable {
    private BigDecimal price;       //秒杀价格
    private Integer killCount;  //秒杀的数量
    private String goodsName;   //商品名称
    private String originalImg; //商品图片

}
