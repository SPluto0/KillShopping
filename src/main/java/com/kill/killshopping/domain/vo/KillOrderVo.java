package com.kill.killshopping.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2019/12/13 9:22
 */
@Data
public class KillOrderVo  implements Serializable {
    private static final long serialVersionUID = 1L;
    private KillGoodsSpecPriceDetailVo killGoodsSpecPriceDetailVo;
    private int addressId;
    private String userId;
}
