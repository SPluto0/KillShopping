package com.kill.killshopping.test.mapper;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.kill.killshopping.test.entity.Goods;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsMapper {
    PageList<Goods> selectList(PageBounds pageBounds);
}
