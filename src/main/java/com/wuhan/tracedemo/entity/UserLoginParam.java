package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Chris
 * @date 2021/7/12 21:54
 * @Email:gem7991@dingtalk.com
 */

@Data
@ToString
public class UserLoginParam {
    private String account;
    private String password;
}
