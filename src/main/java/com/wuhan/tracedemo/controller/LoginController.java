package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.ResponseMsg;
import com.wuhan.tracedemo.common.SalixError;
import com.wuhan.tracedemo.common.log.SalixLog;
import com.wuhan.tracedemo.entity.Merchant;
import com.wuhan.tracedemo.entity.UserLoginParam;
import com.wuhan.tracedemo.service.MerchantService;
import com.wuhan.tracedemo.service.impl.CurrentMerchantService;
import com.wuhan.tracedemo.service.impl.CurrentUserService;
import com.wuhan.tracedemo.utils.JwtUtils;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//@Api(value = "Login", tags = "登录验证")
@CrossOrigin(maxAge = 3600)
@RestController
public class LoginController extends BaseController {

    private final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    MerchantService merchantService;

    @RequestMapping(value = "/merchantlogin", method = RequestMethod.POST)
    public ResponseMsg login(@RequestBody UserLoginParam userLoginParam){
        SalixLog salixLog = new SalixLog();
        salixLog.add("account", userLoginParam.getAccount());
        salixLog.add("password", userLoginParam.getPassword());
        logger.info(salixLog.toString());
        if(userLoginParam.getAccount() == null || userLoginParam.getPassword() == null){
            return ResponseMsg.errorResponse(SalixError.MSG_USER_NAME_PASSWORD_NULL);
        }

        merchantService.authentic(userLoginParam);
        if(!CurrentMerchantService.getMerchant().isAuthenticated()) {
            return ResponseMsg.errorResponse(SalixError.MSG_USER_VERIFY_ERROR);
        }

        Merchant merchant = CurrentMerchantService.getMerchant();
        String jwtToken = JwtUtils.sign(merchant.getUserid(), JwtUtils.SECRET);
        Map<String, Object> result = new HashMap<>();
        result.put("token",jwtToken);
        System.out.println(jwtToken);

        logger.info(merchant.getName() +"登录");

        return ResponseMsg.successResponse(result);
    }

}
