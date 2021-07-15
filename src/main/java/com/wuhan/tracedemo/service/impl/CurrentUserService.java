package com.wuhan.tracedemo.service.impl;

import com.wuhan.tracedemo.entity.User;

/**
 * @author Chris
 * @date 2021/7/13 0:13
 * @Email:gem7991@dingtalk.com
 */
public class CurrentUserService {
    private static ThreadLocal<User> threadLocal = new ThreadLocal();

    public static void setUser(User user){
        threadLocal.set(user);
    }

    public static User getUser(){
        if(threadLocal.get() == null){
            threadLocal.set(new User());
        }
        return threadLocal.get();
    }
}
