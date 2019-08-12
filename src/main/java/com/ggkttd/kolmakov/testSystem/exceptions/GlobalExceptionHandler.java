package com.ggkttd.kolmakov.testSystem.exceptions;

import org.apache.log4j.Logger;
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
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView getNotFoundMessage(Exception ex){
        LOGGER.warn(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("status",HttpStatus.NOT_FOUND);
        modelAndView.addObject("message",ex.getMessage());
        return modelAndView;
    }
}
