package com.wuhan.tracedemo.shiro;


import com.wuhan.tracedemo.entity.Merchant;
import com.wuhan.tracedemo.entity.User;
import com.wuhan.tracedemo.jwt.JwtToken;
import com.wuhan.tracedemo.service.MerchantService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author Chris
 * @date 2021/7/9 12:45
 * @Email:gem7991@dingtalk.com
 */
public class JWTShiroRealm extends AuthorizingRealm {
    @Autowired
    private MerchantService merchantService;

    /**
     * 限定这个 Realm 只处理我们自定义的 JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 用户认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        if (jwtToken.getPrincipal() == null) {
            throw new AccountException("JWT token参数异常！");
        }
        // 从 JwtToken 中获取当前用户
        String userid = authenticationToken.getPrincipal().toString();
        Merchant merchant = merchantService.getById(userid);
        if (merchant == null) {
            throw new UnknownAccountException("用户不存在！");
        } else {
            // 这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(merchant, userid, getName());
            return simpleAuthenticationInfo;
        }
    }

    /**
     * 角色权限和对应权限添加
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        //User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        Merchant currentMerchant = (Merchant) principalCollection.getPrimaryPrincipal();

        // 查询用户名称
        Merchant merchant = merchantService.getById(currentMerchant.getUserid());
        //TODO(a merchant can get all perms)
        //List<String> permissionList = merchantService.queryAllPerms(merchant.getId());
        // 添加角色和权限
        List<String> permissionList = Arrays.asList
                ("logistic:view","logistic:add","logistic:edit", "logistic:delete");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (String p : permissionList) {
            simpleAuthorizationInfo.addStringPermission(p);
        }
        return simpleAuthorizationInfo;
    }
}
