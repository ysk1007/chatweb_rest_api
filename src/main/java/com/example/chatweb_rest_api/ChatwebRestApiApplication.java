package com.example.chatweb_rest_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan // @WebListener가 붙은 리스너가 자동 등록
public class ChatwebRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatwebRestApiApplication.class, args);
	}

}
