package com.kill.killshopping.mybatis.paginator;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.kill.killshopping.KillshoppingApplication;
import com.kill.killshopping.test.entity.Goods;
import com.kill.killshopping.test.mapper.GoodsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Date 2019/12/12 13:40
 */
@SpringBootTest(classes = KillshoppingApplication.class)
public class Demo {
    @Autowired
    GoodsMapper goodsMapper;
    @Test
    public void test_paginator(){
        PageBounds pageBounds = new PageBounds(1,20);
        PageList<Goods> goods = goodsMapper.selectList(pageBounds);
        for (int i = 0; i < 41 ; i++) {
            System.out.println(goods.get(i));
        }
    }
}
