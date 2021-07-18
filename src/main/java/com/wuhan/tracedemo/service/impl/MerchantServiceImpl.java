package com.wuhan.tracedemo.service.impl;

import com.wuhan.tracedemo.common.StatusCode;
import com.wuhan.tracedemo.common.exception.BizException;
import com.wuhan.tracedemo.entity.Merchant;
import com.wuhan.tracedemo.entity.UserLoginParam;
import com.wuhan.tracedemo.mapper.MerchantMapper;
import com.wuhan.tracedemo.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    MerchantMapper merchantMapper;

    public Merchant getById(String id){
        return merchantMapper.getById(id);
    }


    public Merchant getByName(String name){
        return merchantMapper.getByName(name);
    }


    public Long saveMerchant(Merchant merchant) {
        return merchantMapper.insert(merchant);
    }

    public void authentic(UserLoginParam userEntity) {
        Merchant merchant = merchantMapper.getById(userEntity.getAccount());
        System.out.println(userEntity.getAccount());
        System.out.println(merchant);

        if(null == merchant){
            throw new BizException(StatusCode.AUTH_INVALID_CLIENT.getRtCode(), "用户名不存在或联盟中无该用户");
        }

        //TODO(salt is not set, should be set in concat())
        String code = userEntity.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(code.getBytes());
        System.out.println("pwd:"+userEntity.getPassword()+"pwdmd5:" +md5Password);
        if(!merchant.getPassword().equals(md5Password)){
            merchant.setAuthenticated(false);
            throw new BizException(StatusCode.AUTH_INVALID_CLIENT.getRtCode(), "密码错误");
        } else {
            merchant.setAuthenticated(true);
        }
        CurrentMerchantService.setMerchant(merchant);
    }
}
