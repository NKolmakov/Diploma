package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.exceptions.TestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/error1")
    public String getTestException() {
        throwError();
        return "customErrorPage";
    }

    @GetMapping("/error2")
    public String getRuntimeException() {
        throw new RuntimeException();
    }


    @GetMapping("/hi")
    public String getMessage(Model model){
        model.addAttribute("status",500);
        model.addAttribute("message","Hello, World!");
        return "hi";
    }

    private void throwError(){
        throw new TestException();
    }
}
