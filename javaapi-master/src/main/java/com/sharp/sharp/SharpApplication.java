package com.sharp.sharp;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebMvc
@EnableSwagger2
@ControllerAdvice
//@EnableScheduling
@EnableJpaRepositories
public class SharpApplication {

	public static void main(String[] args) throws IOException {
		// System.setProperty("server.servlet.context-path", "/fact6");
		SpringApplication.run(SharpApplication.class, args);
		System.out.println("ssyso");

	}

	

}
