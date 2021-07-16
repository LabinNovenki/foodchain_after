package com.wuhan.tracedemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.wuhan.tracedemo.contract.JRContract;

import java.io.IOException;

/*
@author: Labin
@modified_time:2021/7/16 9:50
@modifier:Labin
* */

@MapperScan("com.wuhan.tracedemo.mapper")
@SpringBootApplication
public class GoodTraceApplication {

	public static void main(String[] args) throws IOException {
		JRContract.initMychainEnv();
		JRContract.initSdk();
		SpringApplication.run(GoodTraceApplication.class, args);
	}

}
