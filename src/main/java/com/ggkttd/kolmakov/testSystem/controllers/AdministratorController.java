package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/administrator")
public class AdministratorController {
    @GetMapping(value = "/home")
    public String getHomePage(ModelMap modelMap, HttpSession session){
        User user = (User)session.getAttribute("user");
        modelMap.addAttribute("user",user);
        return "adminHome";
    }
}
