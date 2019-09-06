package com.ggkttd.kolmakov.testSystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class UtilController {

    @GetMapping(value = "/getProperty")
    public @ResponseBody
    String getProperty(String property, Locale locale) {
        String result = "";
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locales/messages", locale);

        if (property != null) {
            String bundleProperty = resourceBundle.getString(property);
            if (bundleProperty != null) {
                result = new String(bundleProperty.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } else {
                return "";
            }
        }
        return result;
    }
}
