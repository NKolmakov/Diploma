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
//todo: allow tutor save created test as file
//todo: add to administrator responsibility to check registered users
//todo: check if all questions have at least one right answer
//todo: create opportunity to exit from session
//todo: add navigation between answers tutor create test
//todo: set answers amount max=5;
//todo: create html page style with inheritance from bootstrap
//-----------------------------------------------------------------

