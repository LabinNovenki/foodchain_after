package com.wuhan.tracedemo.service.impl;

import com.wuhan.tracedemo.entity.Merchant;

public class CurrentMerchantService {
    private static ThreadLocal<Merchant> threadLocal = new ThreadLocal();

    public static void setMerchant(Merchant merchant){
        threadLocal.set(merchant);
    }

    public static Merchant getMerchant(){
        if(threadLocal.get() == null){
            threadLocal.set(new Merchant());
        }
        return threadLocal.get();
    }
}
