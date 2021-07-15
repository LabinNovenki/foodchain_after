package com.wuhan.tracedemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhan.tracedemo.entity.Good;

/**
 * @author Labin
 * @date 2021/7/13 23:04
 * @Email:gem7991@dingtalk.com
 */
public interface GoodService{
    Good getById(Long id);
    Long saveGood(Good good);
    Good getByName(String name);
}
