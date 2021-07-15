package com.wuhan.tracedemo.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuhan.tracedemo.entity.Good;
import com.wuhan.tracedemo.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.wuhan.tracedemo.entity.Merchant;
import com.wuhan.tracedemo.service.MerchantService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "DataBase", tags = "数据库接口")
@Slf4j
@RestController
public class DatabaseController {
    @Autowired
    GoodService goodService;


    @ResponseBody
    @GetMapping("/good/{id}")
    public Good getGoodById(@RequestParam("id") Long id){
        return goodService.getById(id);
    }

    @ResponseBody
    @GetMapping("/good/{name}")
    public Good getGoodByName(@RequestParam("name") String name){
        return goodService.getByName(name);
    }


    @PostMapping("/good/add")
    public Long saveGood(@RequestBody Good good){
        goodService.saveGood(good);
        return good.getId();
    }

    @Autowired
    MerchantService merchantService;

    @PostMapping("/merchant/add")
    public Long saveMerchant(@RequestBody Merchant merchant) {
        log.info(merchant.toString());
        merchantService.saveMerchant(merchant);
        return merchant.getId();
    }

}
