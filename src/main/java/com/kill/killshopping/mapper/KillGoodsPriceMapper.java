package com.kill.killshopping.mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.kill.killshopping.domain.vo.KillGoodsSpecPriceDetailVo;

public interface KillGoodsPriceMapper {
    /**
     * 查询秒杀商品     PageBounds分页插件    int page, int pageSize    1   ,  100
     * @param pageBounds
     * @return
     */
    PageList<KillGoodsSpecPriceDetailVo> selectView(PageBounds pageBounds);

    /**
     * 查询秒杀商品的详情展示页面
     * @param id
     * @return
     */
    KillGoodsSpecPriceDetailVo detail(Integer id);
}
