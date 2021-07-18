package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.ResponseMsg;
import com.wuhan.tracedemo.entity.*;
import com.wuhan.tracedemo.log.BackLog;
import com.wuhan.tracedemo.service.CommentCodeService;
import com.wuhan.tracedemo.service.LogInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import com.wuhan.tracedemo.service.MerchantService;

import com.wuhan.tracedemo.contract.JRContract;

import java.util.HashMap;
import java.util.Map;

@Api(value = "CommentCode", tags = "评论接口")
@Slf4j
@RestController
public class ContractController {
    @Autowired
    LogInfoService logInfoService;

    private BackLog backLog = new BackLog();

    @Autowired
    MerchantService merchantService;
    @Autowired
    CommentCodeService commentCodeService;

    @ResponseBody
    @PostMapping("/commentCode/commitComment")
    public ResponseMsg commitCommentCode(@RequestBody SimpleComment sim) {
        int block = 0;
        CommentCode commentCode;
        commentCode = commentCodeService.getCommentCode(sim.getCommentid());

        if (commentCode.is_used() == false) {

            // 连接智能合约
            block = JRContract.callContractMakeComment(sim.getCommentid(), sim.getComment(),"",commentCode.getUserid(),sim.getStar());
//            System.out.println(sim.comment);

            commentCodeService.updateCommentCode(sim.getCommentid());
//            LogInfo logInfo = backLog.putLog("non-merchant",  "comment["+ commentCode.getCommentid() +"] was sent successfully",
//                    "/commentCode/commitComment");
//            logInfoService.saveLoginInfo(logInfo);

            return ResponseMsg.successResponse(block);
        } else {
//            LogInfo logInfo = backLog.putLog("non-merchant",  "comment["+ commentCode.getCommentid() +"] was sent, but link's been invalid",
//                    "/commentCode/commitComment");
//            logInfoService.saveLoginInfo(logInfo);
            return ResponseMsg.errorResponse("the link is invalid.");
        }
    }

    @ResponseBody
    @GetMapping("/getComment")
    public ResponseMsg getComment(@RequestParam(value="name") String name) {
        CommentInfo commentInfos[];

        Merchant merchant = merchantService.getByName(name);

        commentInfos = JRContract.callContractQueryComment(merchant.getUserid());
//        SimpleComment[] sim = new SimpleComment[commentInfos.length];

        System.out.println("\n########################################\n");
        float score = 0;

        for (int i = 0; i < commentInfos.length; i++) {
//            sim[i] = new SimpleComment(commentInfos[i].comment, commentInfos[i].time, commentInfos[i].star);
            score += commentInfos[i].star;
        }

        score /= commentInfos.length;
        String scoreString = String.format("%.1f", score);
        ComplexComment complex = new ComplexComment(scoreString, commentInfos);

//        LogInfo logInfo = backLog.putLog("non-merchant",  merchant.getName()+ "was queried",
//                "/getComment");
//        logInfoService.saveLoginInfo(logInfo);

        return ResponseMsg.successResponse(complex);

    }
}