package com.wuhan.tracedemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhan.tracedemo.entity.LogisticInfo;
import com.wuhan.tracedemo.mapper.LogisticMapper;
import com.wuhan.tracedemo.service.LogisticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Chris
 * @date 2021/7/12 23:05
 * @Email:gem7991@dingtalk.com
 */

@Service
public class LogisticServiceImpl extends ServiceImpl<LogisticMapper, LogisticInfo> implements LogisticService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveData(LogisticInfo logistic) {
        logistic.setCreateTime(new Date());
        this.baseMapper.insertData(logistic);
    }
}
