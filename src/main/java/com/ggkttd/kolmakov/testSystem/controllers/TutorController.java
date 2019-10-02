package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Subject;
import com.ggkttd.kolmakov.testSystem.domain.Test;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.exceptions.AccessDeniedException;
import com.ggkttd.kolmakov.testSystem.services.SubjectService;
import com.ggkttd.kolmakov.testSystem.services.TestService;
import com.ggkttd.kolmakov.testSystem.services.UserService;
import com.ggkttd.kolmakov.testSystem.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private TestUtils testUtils;

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
    public String getTestList(@SessionAttribute("user") User user, ModelMap modelMap) {
        List<Test> tests = testService.getByTutorId(user.getId());
        modelMap.addAttribute("selectTest", true);
        modelMap.addAttribute("tests", tests);
        return "mainTutor";
    }

    @GetMapping(value = "/editTest")
    public String getTest2Edit(@SessionAttribute("user") User user, Test test, ModelMap modelMap) {
        Test testFromDb = testService.getOne(test.getId());
        if (!testFromDb.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException();
        }
        testFromDb = testService.sortQuestionsByDesc(testFromDb);
        List<Subject> subjects = subjectService.getAll();
        subjects.remove(testFromDb.getSubject());
        modelMap.addAttribute("editTest", true);
        modelMap.addAttribute("test", testFromDb);
        modelMap.addAttribute("subjects", subjects);
        return "mainTutor";
    }

    @GetMapping(value = "/deleteTest")
    public String getTestList2Delete(@SessionAttribute("user") User user, ModelMap modelMap) {
        List<Test> tests = testService.getByTutorId(user.getId());
        modelMap.addAttribute("deleteTest", true);
        modelMap.addAttribute("tests", tests);
        return "mainTutor";
    }

    @GetMapping(value = "/getTestGrid")
    public String getTestListAsGrid(@SessionAttribute("user") User user, ModelMap modelMap) {
        List<Test> tests = testService.getByTutorId(user.getId());
        modelMap.addAttribute("testGrid", true);
        modelMap.addAttribute("tests", tests);
        return "mainTutor";
    }

    @GetMapping(value = "/generateTest")
    public String getGeneratedTest(@RequestParam("doc") MultipartFile multipartFile, @SessionAttribute("user") User user) {

    }

    @PostMapping(value = "/createTest")
    public String saveTest(@SessionAttribute("user") User user, Test test, ModelMap modelMap, Locale locale,
                           @RequestParam(name = "file", required = false) MultipartFile[] multipartFiles) {
        test.setOwner(user);
        test.setFiles(multipartFiles);
        testService.save(test);
        modelMap.addAttribute("success", true);
        modelMap.addAttribute("message", messageSource.getMessage("message.notif.testSaved", new Object[]{}, locale));
        return "mainTutor";
    }

    @PostMapping(value = "/editTest")
    public String getEditedTest(@SessionAttribute("user") User user, Test test, ModelMap modelMap) {
        test.setOwner(user);
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
