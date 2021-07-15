package com.wuhan.tracedemo.entity;

import lombok.Data;
import lombok.ToString;
import sun.rmi.runtime.Log;

import java.util.Date;

/**
 * @author Chris
 * @date 2021/7/12 23:00
 * @Email:gem7991@dingtalk.com
 */

@Data
@ToString
public class LogisticInfo {
    private Long id;
    private Long goodId;
    private String description;
    private Integer status;
    private Date createTime;
}
