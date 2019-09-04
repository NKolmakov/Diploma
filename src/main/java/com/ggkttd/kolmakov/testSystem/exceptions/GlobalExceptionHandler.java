package com.ggkttd.kolmakov.testSystem.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
public class GlobalExceptionHandler{
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    public ModelAndView getNotFoundMessage(Exception ex){
        ex.printStackTrace();
        LOGGER.warn(ex.getStackTrace().toString());
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("status",HttpStatus.NOT_FOUND);
        modelAndView.addObject("message",ex.getMessage());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView getNotFoundMessage(){
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("status",HttpStatus.NOT_FOUND);
        modelAndView.addObject("message","resource not found");
        return modelAndView;
    }
}
