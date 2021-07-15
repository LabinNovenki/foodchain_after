package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Merchant {
    private Long id;
    private String name;
    private String password;
    private String address;
    private Date set_time;
    private String introduction;
    private String phone;

    private boolean isAuthenticated;
    private int status;
}
