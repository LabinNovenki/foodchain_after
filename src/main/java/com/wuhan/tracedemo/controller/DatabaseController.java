package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.ResponseMsg;
import com.wuhan.tracedemo.entity.CommentCode;
import com.wuhan.tracedemo.service.CommentCodeService;
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
    public ResponseMsg saveMerchant(@RequestBody Merchant merchant) {
        log.info(merchant.toString());
        merchant.md5password();
        merchantService.saveMerchant(merchant);
        return ResponseMsg.successResponse("successfully sigh up");
    }

    @ResponseBody
    @GetMapping("/merchantId")
    public Merchant getMerchantById(@RequestParam("id") String id){
        return merchantService.getById(id);
    }

    @ResponseBody
    @GetMapping("/merchantName")
    public Merchant getMerchantByName(@RequestParam("name") String name){
        return merchantService.getByName(name);
    }

    @Autowired
    CommentCodeService commentCodeService;
    @GetMapping("/commentCode/saveCommentCode")
    public ResponseMsg saveCommentCode(@RequestParam("user") String userid) {
        CommentCode commentCode;
        commentCode = commentCodeService.createCommentCode(userid);
        commentCodeService.saveCommentCode(commentCode);
        Merchant merchant = merchantService.getById(userid);
        String merchantName = merchant.getName();
        String url = "http://localhost:8081/comment?name="
                + merchantName
                + "&tokenid=" + commentCode.getCommentid();
        return ResponseMsg.successResponse(url);
    }

    /*
    @GetMapping("/commentCode/checkCommentCode")
    public boolean commitCommentCode(@RequestParam(value="id") String commentid,
                                     @RequestParam(value="comment") String comment) {
        CommentCode commentCode;
        commentCode = commentCodeService.getCommentCode(commentid);

        if (commentCode.is_used() == false) {

            // 连接智能合约
            System.out.println(comment);

            commentCodeService.updateCommentCode(commentid);
            return true;
        } else {
            return false;
        }
    }*/
}
