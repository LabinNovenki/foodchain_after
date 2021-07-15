package com.wuhan.tracedemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhan.tracedemo.entity.Good;
import com.wuhan.tracedemo.mapper.GoodMapper;
import com.wuhan.tracedemo.service.GoodService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhan.tracedemo.common.StatusCode;
import com.wuhan.tracedemo.common.exception.BizException;
import com.wuhan.tracedemo.entity.User;
import com.wuhan.tracedemo.entity.UserLoginParam;
import com.wuhan.tracedemo.mapper.UserMapper;
import com.wuhan.tracedemo.service.UserService;
import com.wuhan.tracedemo.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;



@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    GoodMapper goodMapper;

    public Good getById(Long id){
        return goodMapper.getById(id);
    }


    public Good getByName(String name){
        return goodMapper.getByName(name);
    }


    public Long saveGood(Good good) {
        return goodMapper.insert(good);
    }
}
