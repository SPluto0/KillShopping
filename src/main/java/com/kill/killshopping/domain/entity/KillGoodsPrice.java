package com.kill.killshopping.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Date 2019/12/13 9:55
 */
@Data
public class KillGoodsPrice implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private Integer specGoodsId;

    private Integer status;

    private BigDecimal price;

    private Integer killCount;

    private Date begainTime;

    private Date endTime;

}
