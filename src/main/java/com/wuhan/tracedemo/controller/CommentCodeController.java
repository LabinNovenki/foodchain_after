package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.ResponseMsg;
import com.wuhan.tracedemo.entity.CommentCode;
import com.wuhan.tracedemo.service.CommentCodeService;
import io.swagger.annotations.Api;
import org.apache.ibatis.jdbc.Null;
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
import com.wuhan.tracedemo.contract.CommentInfo;
import com.wuhan.tracedemo.contract.JRContract;

@Api(value = "CommentCode", tags = "评论接口")
@Slf4j
@RestController
public class CommentCodeController {
    MerchantService merchantService;
    @Autowired
    CommentCodeService commentCodeService;
    @GetMapping("/commentCode/getComment")
    public String saveCommentCode(@RequestParam("user") String userid) {
        CommentCode commentCode;
        commentCode = commentCodeService.createCommentCode(userid);
        commentCodeService.saveCommentCode(commentCode);
        String url = "http://localhost:8816/commentCode/writeComment?user="
                + merchantService.getById(userid)
                + "&id=" + commentCode.getCommentid();
        return url;
    }

    @GetMapping("/commentCode/commitComment")
    public boolean commitCommentCode(@RequestParam(value="id") String commentid,
                                     @RequestParam(value="comment") String comment) {
        CommentCode commentCode;
        commentCode = commentCodeService.getCommentCode(commentid);

        if (commentCode.is_used() == false) {

            // 连接智能合约
            JRContract.callContractMakeComment(comment,"",commentCode.getUserid());
            System.out.println(comment);

            commentCodeService.updateCommentCode(commentid);
            return true;
        } else {
            return false;
        }
    }
    @GetMapping("/test")
    public void test(@RequestParam(value="id") String userid) {
        CommentInfo commentInfos[];
        commentInfos = JRContract.callContractQueryComment(userid);
        System.out.println("\n########################################\n");
        for (int i = 0; i < commentInfos.length; i++) {
            System.out.println(commentInfos[i].comment);
            System.out.println(commentInfos[i].time);
        }
    }
}
