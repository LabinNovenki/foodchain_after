package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.DigestUtils;

import java.util.Date;

@Data
@ToString
public class Merchant {
    private Long id;
    private String name;
    private String userid;
    private String password;
    private String address;
    private Date set_time;
    private String introduction;
    private String phone;
    public void md5password() {
        String code = password;
        String md5Password = DigestUtils.md5DigestAsHex(code.getBytes());
        password = md5Password;
    }

    private boolean isAuthenticated;
    private int status;
}
