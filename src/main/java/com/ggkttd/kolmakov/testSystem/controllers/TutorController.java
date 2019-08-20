package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Subject;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.services.SubjectService;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "/tutor")
public class TutorController {
    @Autowired
    private TestService testService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SubjectService subjectService;

    @GetMapping(value = "/mainTutor")
    public String getHomePage(ModelMap modelMap, HttpSession session){
        User user = (User)session.getAttribute("user");
        modelMap.addAttribute("user",user);
        return "mainTutor";
    }

    @GetMapping(value = "/createTest")
    public String getCreateTestPage(ModelMap modelMap){
        List<Subject> subjects = subjectService.getAll();
        modelMap.addAttribute("createTest",true);
        modelMap.addAttribute("subjects",subjects);
        return "mainTutor";
    }

    @PostMapping(value = "/createTest")
    public String saveTest(Test test,ModelMap modelMap, Locale locale){
//        testService.save(test);
        modelMap.addAttribute("success",true);
        modelMap.addAttribute("message",messageSource.getMessage("message.notif.testSaved",new Object[]{},locale));
        return "mainTutor";
    }
}
