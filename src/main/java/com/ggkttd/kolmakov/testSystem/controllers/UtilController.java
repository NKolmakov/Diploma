package com.ggkttd.kolmakov.testSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Controller
public class UtilController {
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @GetMapping(value = "/getProperty")
    public @ResponseBody
    String getProperty(String property) {
        return resourceBundle.getString(property);
    }
}
