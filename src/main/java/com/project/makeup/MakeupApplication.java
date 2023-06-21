package com.project.makeup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.swagger.v3.oas.models.annotations.OpenAPI31;

@SpringBootApplication
@EnableWebMvc
@OpenAPI31
public class MakeupApplication {

	public static void main(String[] args) {
		SpringApplication.run(MakeupApplication.class, args);
	}

}
