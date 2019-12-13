package com.kill.killshopping.service.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.kill.killshopping.domain.vo.KillGoodsSpecPriceDetailVo;
import com.kill.killshopping.mapper.KillGoodsPriceMapper;
import com.kill.killshopping.service.IKillSpecManageService;
import com.kill.killshopping.util.GridModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Date 2019/12/13 10:00
 */
@Service
public class IKillSpecManageServiceImpl implements IKillSpecManageService {
    @Resource
    private KillGoodsPriceMapper killGoodsPriceMapper;
    @Override
    public GridModel<KillGoodsSpecPriceDetailVo> queryView(int page, int pageSize) {
        PageBounds pageBounds = new PageBounds(page, pageSize);
        return new GridModel<>(killGoodsPriceMapper.selectView(pageBounds));
    }

    @Override
    public KillGoodsSpecPriceDetailVo detailById(Integer id) {
        return killGoodsPriceMapper.detail(id);
    }
}
