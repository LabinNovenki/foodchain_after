package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.ResponseMsg;
import com.wuhan.tracedemo.entity.CommentCode;
import com.wuhan.tracedemo.entity.LogInfo;
import com.wuhan.tracedemo.service.CommentCodeService;
import com.wuhan.tracedemo.service.LogInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.wuhan.tracedemo.log.BackLog;





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
    LogInfoService logInfoService;

    private BackLog backLog = new BackLog();

    @Autowired
    MerchantService merchantService;

    @PostMapping("/merchant/add")
    public ResponseMsg saveMerchant(@RequestBody Merchant merchant) {
        log.info(merchant.toString());
        merchant.md5password();
        merchantService.saveMerchant(merchant);
        LogInfo logInfo = backLog.putLog(merchant.getUserid(), merchant.getName() + "sign up.",
                "/merchant/add");
        logInfoService.saveLoginInfo(logInfo);
        return ResponseMsg.successResponse("successfully sigh up");
    }

    @ResponseBody
    @GetMapping("/merchantId")
    public Merchant getMerchantById(@RequestParam("id") String id){
        LogInfo logInfo = backLog.putLog("non-merchant", "get merchant by id:[" + id + "]",
                "/merchantId");
        logInfoService.saveLoginInfo(logInfo);
        return merchantService.getById(id);
    }

    @ResponseBody
    @GetMapping("/merchantName")
    public Merchant getMerchantByName(@RequestParam("name") String name){
        LogInfo logInfo = backLog.putLog("non-merchant", "get merchant by name:[" + name + "]",
                "/merchantName");
        logInfoService.saveLoginInfo(logInfo);
        return merchantService.getByName(name);
    }


    @Autowired
    CommentCodeService commentCodeService;
    @GetMapping("/commentCode/saveCommentCode")
    public ResponseMsg saveCommentCode(@RequestParam("user") String userid) {


//        BackLog backLog = new BackLog();
//        backLog.putLog("null", "get merchant by name", "/commentCode/saveCommentCode");

        CommentCode commentCode;
        commentCode = commentCodeService.createCommentCode(userid);
        commentCodeService.saveCommentCode(commentCode);
        Merchant merchant = merchantService.getById(userid);
        String merchantName = merchant.getName();
        String url = "http://localhost:8081/comment?name="
                + merchantName
                + "&tokenid=" + commentCode.getCommentid();
        LogInfo logInfo = backLog.putLog(userid, merchant.getName() + "ask for a commentURL:[" + url +"]",
                "/commentCode/saveCommentCode");
        logInfoService.saveLoginInfo(logInfo);
        return ResponseMsg.successResponse(url);
    }
}
