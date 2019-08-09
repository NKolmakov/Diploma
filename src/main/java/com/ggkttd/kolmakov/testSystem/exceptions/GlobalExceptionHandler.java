package com.ggkttd.kolmakov.testSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler{

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(value = TestException.class)
    public ModelAndView exceptionClassHandler(HttpServletRequest request, Model model, Exception ex){
       // LOGGER.info("Error: "+ex.getMessage());
        model.addAttribute("status",500);
        model.addAttribute("message","INTERNAL_SERVER_ERROR");
        return new ModelAndView("customErrorPage");
    }

//    @ExceptionHandler(value = Throwable.class)
//    public ModelAndView throwableClassHandler(){
//        return new ModelAndView("customErrorPage");
//    }
}
