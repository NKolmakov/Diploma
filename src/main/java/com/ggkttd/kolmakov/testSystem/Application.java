package com.ggkttd.kolmakov.testSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan("com.ggkttd.kolmakov.testSystem")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
//-----------------------------------------------------------------
//todo: fix student passing test

//registration
//authorization
//error for authorization/registration
//success for authorization/registration
//homeStudentPage
//homeTutorPage
//homeAdministratorPage
