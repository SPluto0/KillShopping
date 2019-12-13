package com.kill.killshopping.mapper;

import com.kill.killshopping.domain.entity.UserAddress;

public interface UserAddressMapper {
    /**
     *  查询用户的收货信息
     * @param addressId
     * @return
     */
    UserAddress selectByPrimaryKey(int addressId);
}
