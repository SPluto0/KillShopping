package com.kill.killshopping.service;

import com.kill.killshopping.domain.vo.KillGoodsSpecPriceDetailVo;
import com.kill.killshopping.util.GridModel;

public interface IKillSpecManageService {
    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    GridModel<KillGoodsSpecPriceDetailVo> queryView(int page, int pageSize);

    /**
     *
     * @param id
     * @return
     */
    KillGoodsSpecPriceDetailVo detailById(Integer id);
}
