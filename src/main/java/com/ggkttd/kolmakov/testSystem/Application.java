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
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("123"));
	}
}

//todo: add error message in error view
//todo: how to send localized message to view
//todo: add institution of education to application
//todo: figure out if exists inheritance in html classes view

//TODO: STATIC PAGES LIST

//registration
//authorization
//error for authorization/registration
//success for authorization/registration
//homeStudentPage
//homeTutorPage
//homeAdministratorPage
