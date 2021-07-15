package com.wuhan.tracedemo.controller;

import com.wuhan.tracedemo.common.log.SalixLog;
import io.swagger.annotations.Api;

/**
 * @author Chris
 * @date 2021/7/12 21:50
 * @Email:gem7991@dingtalk.com
 */
@Api(value = "Base", tags = "基本接口")
public class BaseController {
    private static ThreadLocal<SalixLog> logThreadLocal = new ThreadLocal<>();

    public SalixLog getSalixLog() {
        return logThreadLocal.get();
    }

    public SalixLog initSalixLog() {
        logThreadLocal.set(new SalixLog());
        return logThreadLocal.get();
    }
}
