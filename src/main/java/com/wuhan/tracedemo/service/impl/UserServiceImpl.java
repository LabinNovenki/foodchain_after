package com.wuhan.tracedemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhan.tracedemo.common.StatusCode;
import com.wuhan.tracedemo.common.exception.BizException;
import com.wuhan.tracedemo.entity.User;
import com.wuhan.tracedemo.entity.UserLoginParam;
import com.wuhan.tracedemo.mapper.MerchantMapper;
import com.wuhan.tracedemo.mapper.UserMapper;
import com.wuhan.tracedemo.service.UserService;
import com.wuhan.tracedemo.utils.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.List;


/**
 * @author Chris
 * @date 2021/7/12 21:47
 * @Email:gem7991@dingtalk.com
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public void authentic(UserLoginParam userEntity) {
        User user = baseMapper.getUserByName(userEntity.getAccount()) ;

        if(null == user){
            throw new BizException(StatusCode.AUTH_INVALID_CLIENT.getRtCode(), "用户名不存在或联盟中无该用户");
        }

        String code = userEntity.getAccount().concat(userEntity.getPassword()).concat(user.getSalt()) ;
        String md5Password = DigestUtils.md5DigestAsHex(code.getBytes());
        System.out.println("pwd:"+userEntity.getPassword()+"pwdmd5:" +md5Password);
        if(!user.getPassword().equals(md5Password)){
            user.setAuthenticated(false);
            throw new BizException(StatusCode.AUTH_INVALID_CLIENT.getRtCode(), "密码错误");
        } else {
            user.setAuthenticated(true);
        }
        CurrentUserService.setUser(user);
    }

    @Override
    public User getUserByName(String userName) {
        return baseMapper.getUserByName(userName);
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        String salt = JwtUtils.generateSalt();
        String code = username.concat(newPassword).concat(salt) ;
        String md5Password = DigestUtils.md5DigestAsHex(code.getBytes());
        baseMapper.updatePassword(username,md5Password,salt);
    }


}
