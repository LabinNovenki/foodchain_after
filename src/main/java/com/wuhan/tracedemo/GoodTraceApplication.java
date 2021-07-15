package com.wuhan.tracedemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.wuhan.tracedemo.mapper")
@SpringBootApplication
public class GoodTraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodTraceApplication.class, args);
	}

}
