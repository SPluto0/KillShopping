package com.kill.killshopping;

import com.kill.killshopping.domain.vo.KillGoodsSpecPriceDetailVo;
import com.kill.killshopping.service.KillGoodsService;
import com.kill.killshopping.util.GridModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = KillshoppingApplication.class)
class KillshoppingApplicationTests {
	@Resource
	KillGoodsService killGoodsService;
	Integer[]killId = new Integer[]{19,21,22,23};
	String[]userId = new String[]{"1","10","20","30"};

	int addressId = 1;
	@Test
	void contextLoads() {
        GridModel<KillGoodsSpecPriceDetailVo> killGoodsSpecPriceDetailVoGridModel = killGoodsService.queryByPage();
        for (KillGoodsSpecPriceDetailVo killGoodsSpecPriceDetailVo : killGoodsSpecPriceDetailVoGridModel.getRows()) {
            System.out.println(killGoodsSpecPriceDetailVo);
        }

        KillGoodsSpecPriceDetailVo detail = killGoodsService.detail(killId[0]);
        System.out.println(detail);

//        if(killGoodsService.getKillGoods(killId[0], userId[0])){
            if(killGoodsService.kill(killId[0].toString(),userId[0])) {
                String order = killGoodsService.submitOrder(addressId, killId[0], userId[0]);
                System.out.println(order);
            }
//        }




	}

}
