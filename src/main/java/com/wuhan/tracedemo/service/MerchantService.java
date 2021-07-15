package com.wuhan.tracedemo.service;

import com.wuhan.tracedemo.entity.Merchant;
import com.wuhan.tracedemo.entity.UserLoginParam;

/**
 * @author Labin
 * @date 2021/7/13 23:04
 * @Email:gem7991@dingtalk.com
 */
public interface MerchantService {
    Merchant getById(Long id);
    void authentic(UserLoginParam userEntity);
    Long saveMerchant(Merchant merchant);
    Merchant getByName(String name);
}
