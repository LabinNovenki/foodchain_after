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
    MerchantService merchantService;
    @Autowired
    CommentCodeService commentCodeService;


    @PostMapping("/commentCode/commitComment")
    public int commitCommentCode(@RequestBody SimpleComment sim) {
        int block = 0;
        CommentCode commentCode;
        commentCode = commentCodeService.getCommentCode(sim.getCommentid());

        if (commentCode.is_used() == false) {

            // 连接智能合约
            block = JRContract.callContractMakeComment(sim.getComment(),"",commentCode.getUserid(),sim.getStar());
//            System.out.println(sim.comment);

            commentCodeService.updateCommentCode(sim.getCommentid());
            return block;
        } else {
            return block;
        }
    }

    @ResponseBody
    @GetMapping("/getComment")
    public ResponseMsg getComment(@RequestParam(value="userid") String userid) {
        CommentInfo commentInfos[];

        commentInfos = JRContract.callContractQueryComment(userid);
        SimpleComment[] sim = new SimpleComment[commentInfos.length];

        System.out.println("\n########################################\n");
        float score = 0;

        for (int i = 0; i < commentInfos.length; i++) {
            System.out.println(commentInfos[i].comment);
            System.out.println(commentInfos[i].time);
            System.out.println(commentInfos[i].star);
            sim[i] = new SimpleComment(commentInfos[i].comment, commentInfos[i].time, commentInfos[i].star);
            score += commentInfos[i].star;
        }

        score /= commentInfos.length;
        System.out.println(score);
        ComplexComment complex = new ComplexComment(score, sim);
        return ResponseMsg.successResponse(complex);
//        return ResponseMsg.successResponse(sim);
    }
}