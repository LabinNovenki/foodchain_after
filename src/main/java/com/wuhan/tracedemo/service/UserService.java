package com.wuhan.tracedemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhan.tracedemo.entity.User;
import com.wuhan.tracedemo.entity.UserLoginParam;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * @author Chris
 * @date 2021/7/12 21:45
 * @Email:gem7991@dingtalk.com
 */

public interface UserService extends IService<User>{
    List<String> queryAllPerms(Long userId);
    void authentic(UserLoginParam userEntity);
    User getUserByName(String userName);
    void updatePassword(String username, String newPassword);
}
