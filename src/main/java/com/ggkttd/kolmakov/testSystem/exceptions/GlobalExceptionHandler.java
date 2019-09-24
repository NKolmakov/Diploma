package com.ggkttd.kolmakov.testSystem.exceptions;

//@ControllerAdvice
//public class GlobalExceptionHandler{
//    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(value = Throwable.class)
//    public ModelAndView getNotFoundMessage(Exception ex){
//        LOGGER.warn(ex);
//        ModelAndView modelAndView = new ModelAndView("errorPage");
//        modelAndView.addObject("status",HttpStatus.NOT_FOUND);
//        modelAndView.addObject("message",ex.getMessage());
//        return modelAndView;
//    }
//
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView getNotFoundMessage(){
//        ModelAndView modelAndView = new ModelAndView("errorPage");
//        modelAndView.addObject("status",HttpStatus.NOT_FOUND);
//        modelAndView.addObject("message","resource not found");
//        return modelAndView;
//    }
//}
