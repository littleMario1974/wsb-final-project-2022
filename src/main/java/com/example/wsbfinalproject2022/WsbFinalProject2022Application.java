package com.example.wsbfinalproject2022;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WsbFinalProject2022Application {

	public static void main(String[] args) {
		SpringApplication.run(WsbFinalProject2022Application.class, args);
	}

}
