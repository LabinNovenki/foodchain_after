package com.wuhan.tracedemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhan.tracedemo.entity.LogisticInfo;

/**
 * @author Chris
 * @date 2021/7/12 23:04
 * @Email:gem7991@dingtalk.com
 */
public interface LogisticService extends IService<LogisticInfo> {
    void saveData(LogisticInfo logistic);
}
