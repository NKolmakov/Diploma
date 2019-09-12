package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Group;
import com.ggkttd.kolmakov.testSystem.domain.Role;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.services.GroupService;
import com.ggkttd.kolmakov.testSystem.services.UserService;
import com.ggkttd.kolmakov.testSystem.utils.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

@Controller
public class AuthorizationController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @GetMapping(value = "/")
    public String getIndexPage() {
        return "authorization";
    }

    @GetMapping(value = "/authorization")
    public String getAuthorizationForm() {
        return "authorization";
    }

    @GetMapping(value = "/registration")
    public String getRegistrationForm(ModelMap modelMap) {
        List<Group> groups = groupService.getAll();
        modelMap.addAttribute("groups", groups);
        return "registration";
    }

    @GetMapping(value = "/exit")
    public String exit(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/authorization";
    }

    @PostMapping(value = "/authorization")
    public String resolveMainPage(User userFromClient, ModelMap modelMap, HttpSession session, Locale locale) {
        User user = userService.checkAuthorization(userFromClient);
        if (user.isAuthorized()) {
            session.setAttribute("user", user);
            UserRoles role = user.getRole().getName();
            if (role == UserRoles.STUDENT) {
                return "redirect:/student/mainStudent";
            }
            if (role == UserRoles.TUTOR) {
                return "redirect:/tutor/mainTutor";
            }
            if (role == UserRoles.ADMINISTRATOR) {
                return "redirect:/administrator/mainAdministrator";
            }
        } else {
            modelMap.addAttribute("message", messageSource.getMessage("message.notif.userNotFound", new Object[]{}, locale));
            modelMap.addAttribute("error", true);
        }
        return "authorization";
    }


    @PostMapping(value = "/registration")
    public String signUp(User userFromClient, ModelMap modelMap, Locale locale) {
        List<Group> groups = groupService.getAll();
        modelMap.addAttribute("groups", groups);
        User userFromDb = userService.findByLogin(userFromClient);

        //check if such user exists
        if (userFromDb != null) {
            modelMap.addAttribute("error", true);
            modelMap.addAttribute("message", messageSource.getMessage("message.notif.userExists", new Object[]{}, locale));
        } else {
            userService.save(userFromClient);
            modelMap.addAttribute("success", true);
            modelMap.addAttribute("message", messageSource.getMessage("message.notif.regSuccess", new Object[]{}, locale));
        }

        //after registration process show registration form with registration result
        return "registration";
    }

    @GetMapping(value = "/home")
    public String resolveHomePage(@SessionAttribute(value = "user", required = false) User user) {
        if (user != null && user.isAuthorized()) {
            UserRoles role = user.getRole().getName();
            if (role == UserRoles.STUDENT) {
                return "redirect:/student/mainStudent";
            }
            if (role == UserRoles.TUTOR) {
                return "redirect:/tutor/mainTutor";
            }
            if (role == UserRoles.ADMINISTRATOR) {
                return "redirect:/administrator/mainAdministrator";
            }
        }

        return "redirect:/authorization";
    }
}
