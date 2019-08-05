package com.ggkttd.kolmakov.testSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@ControllerAdvice
public class GlobalExceptionHandler {
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = Exception.class)
//    public String getInternalServerError(Model model,Exception ex){
//        model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);
//        model.addAttribute("message",ex.getMessage());
//        return "error";
//    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    public String getIRuntimeException(Model model,Exception ex){
        model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message","this is message");
        return "error";
    }
}
