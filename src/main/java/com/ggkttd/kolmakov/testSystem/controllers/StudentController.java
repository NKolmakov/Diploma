package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.*;
import com.ggkttd.kolmakov.testSystem.services.AnswerLogService;
import com.ggkttd.kolmakov.testSystem.services.PassingTestService;
import com.ggkttd.kolmakov.testSystem.services.SubjectService;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TestService testService;
    @Autowired
    private PassingTestService passingTestService;

    @GetMapping(value = "/mainStudent")
    public String getHomePage(ModelMap modelMap,HttpSession session){
        User user = (User)session.getAttribute("user");
        modelMap.addAttribute("user",user);
        return "mainStudent";
    }

    @GetMapping(value = "/getSubjects")
    public String getSubjects(ModelMap modelMap){
        List<Subject> subjects = subjectService.getAll();
        modelMap.addAttribute("subjects",subjects);
        modelMap.addAttribute("showSubjects",true);
        return "mainStudent";
    }

    @GetMapping(value = "/getTests")
    public String getTests(Subject subject,HttpSession session,ModelMap modelMap){
        User user = (User)session.getAttribute("user");
        List<Test> tests = testService.getNotPassed(user.getId(),subject.getId());
        modelMap.addAttribute("tests",tests);
        modelMap.addAttribute("showTests",true);
        return "mainStudent";
    }

    @GetMapping(value = "/passTest")
    public String getTest(Test test, ModelMap modelMap){
        Test testFromDb = testService.getOne(test.getId());
        modelMap.addAttribute("test",testFromDb);
        modelMap.addAttribute("passTest",true);
        return "mainStudent";
    }

    @PostMapping(value = "/passTest")
    public String saveStudentTest(PassingTest passingTest,HttpSession session, ModelMap modelMap){
        User user = (User)session.getAttribute("user");
        passingTest.setUserId(user.getId());
        passingTestService.save(passingTest);
        return "redirect:mainStudent";
    }
}
