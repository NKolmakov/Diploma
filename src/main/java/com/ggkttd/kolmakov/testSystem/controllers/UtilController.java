package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import com.ggkttd.kolmakov.testSystem.utils.TestUtils;
import com.ibm.icu.text.Transliterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class UtilController {
    @Autowired
    private TestUtils utils;
    @Autowired
    private TestService testService;

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

    @GetMapping(value = "/getDocument")
    public void getDocumentFromTest(Long testId, HttpServletResponse response) {
        Transliterator transliterator = Transliterator.getInstance("Any-Latin");
        Test test = testService.getOne(testId);
        File docxTest = utils.saveTest2File(test);
        response.setHeader("Content-Description", "File Transfer");
        response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + transliterator.transliterate(docxTest.getName()));
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Length", String.valueOf(docxTest.length()));
        try
        {
            Files.copy(docxTest.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
            docxTest.delete();
        } catch (IOException e) {
            docxTest.delete();
            e.printStackTrace();
        }
    }
}
