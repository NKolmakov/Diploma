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
//todo: set test timer
//todo: create start test time as session attribute
//todo: allow tutor save created test as file
//todo: get localize messages from js file
//todo: set start/end time working with test

//TODO: STOPPED AT SENDING START/END TIME ON FORM SUBMIT

//-----------------------------------------------------------------

