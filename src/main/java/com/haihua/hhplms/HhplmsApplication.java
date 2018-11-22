package com.haihua.hhplms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class HhplmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HhplmsApplication.class, args);
	}
}
