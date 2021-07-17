package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.ResponseMsg;
import com.wuhan.tracedemo.entity.*;
import com.wuhan.tracedemo.service.CommentCodeService;
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
    MerchantService merchantService;
    @Autowired
    CommentCodeService commentCodeService;


    @PostMapping("/commentCode/commitComment")
    public boolean commitCommentCode(@RequestBody SimpleComment sim) {
        CommentCode commentCode;
        commentCode = commentCodeService.getCommentCode(sim.getCommentid());

        if (commentCode.is_used() == false) {

            // 连接智能合约
            JRContract.callContractMakeComment(sim.getComment(),"",commentCode.getUserid());
//            System.out.println(sim.comment);

            commentCodeService.updateCommentCode(sim.getCommentid());
            return true;
        } else {
            return false;
        }
    }

    @ResponseBody
    @GetMapping("/getComment")
    public ResponseMsg getComment(@RequestParam(value="name") String name) {
        CommentInfo commentInfos[];

        Merchant merchant = merchantService.getByName(name);

        commentInfos = JRContract.callContractQueryComment(merchant.getUserid());
        SimpleComment[] sim = new SimpleComment[commentInfos.length];

        System.out.println("\n########################################\n");
        for (int i = 0; i < commentInfos.length; i++) {
            System.out.println(commentInfos[i].comment);
            System.out.println(commentInfos[i].time);
            sim[i] = new SimpleComment(commentInfos[i].comment, commentInfos[i].time);
//            sim[i].setComment(commentInfos[i].comment);
//            sim[i].setCommentid(commentInfos[i].time);
            System.out.println("sim:"+sim[i].getComment());
        }
        return ResponseMsg.successResponse(sim);
    }
}