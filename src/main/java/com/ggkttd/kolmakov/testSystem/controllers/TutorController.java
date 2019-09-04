package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Subject;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.services.SubjectService;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import com.ggkttd.kolmakov.testSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

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
    @Autowired
    private UserService userService;

    @GetMapping(value = "/mainTutor")
    public String getHomePage(ModelMap modelMap, HttpSession session) {
        User user = (User) session.getAttribute("user");
        modelMap.addAttribute("user", user);
        return "mainTutor";
    }

    @GetMapping(value = "/createTest")
    public String getCreateTestPage(ModelMap modelMap) {
        List<Subject> subjects = subjectService.getAll();
        modelMap.addAttribute("createTest", true);
        modelMap.addAttribute("subjects", subjects);
        return "mainTutor";
    }

    @GetMapping(value = "/getTestList")
    public String getTestList(ModelMap modelMap) {
        List<Test> tests = testService.findAll();
        modelMap.addAttribute("selectTest", true);
        modelMap.addAttribute("tests", tests);
        return "mainTutor";
    }

    @GetMapping(value = "/editTest")
    public String getTest2Edit(Test test, ModelMap modelMap) {
        Test testFromDb = testService.getOne(test.getId());
        testFromDb = testService.sortQuestionsByDesc(testFromDb);
        List<Subject> subjects = subjectService.getAll();
        if (subjects.contains(testFromDb.getSubject())) {
            subjects.remove(testFromDb.getSubject());
        }
        modelMap.addAttribute("editTest", true);
        modelMap.addAttribute("test", testFromDb);
        modelMap.addAttribute("subjects", subjects);
        return "mainTutor";
    }

    @GetMapping(value = "/deleteTest")
    public String getTestList2Delete(ModelMap modelMap) {
        List<Test> tests = testService.findAll();
        modelMap.addAttribute("deleteTest", true);
        modelMap.addAttribute("tests", tests);
        return "mainTutor";
    }

    @PostMapping(value = "/createTest")
    public String saveTest(@SessionAttribute("user") User user, Test test, ModelMap modelMap, Locale locale) {
//        User userFromDb = userService.getUser(user.getId());
        test.setOwner(user);
        testService.save(test);
        modelMap.addAttribute("success", true);
        modelMap.addAttribute("message", messageSource.getMessage("message.notif.testSaved", new Object[]{}, locale));
        return "mainTutor";
    }

    @PostMapping(value = "/editTest")
    public String getEditedTest(Test test, ModelMap modelMap) {
        testService.save(test);
        return "mainTutor";
    }

    @PostMapping(value = "/deleteTest")
    public String deleteTest(Test test, ModelMap modelMap) {
        Test testFromDb = testService.getOne(test.getId());
        testService.delete(testFromDb);
        return "mainTutor";
    }
}
