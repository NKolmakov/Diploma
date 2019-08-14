package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Group;
import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.services.GroupService;
import com.ggkttd.kolmakov.testSystem.services.UserService;
import com.ggkttd.kolmakov.testSystem.utils.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthorizationController {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @GetMapping(value = "/")
    public String getIndexPage(){
        return "authorization";
    }

    @GetMapping(value = "/authorization")
    public String getAuthorizationForm(){
        return "authorization";
    }

    @GetMapping(value = "/registration")
    public String getRegistrationForm(ModelMap modelMap){
        List<Group> groups = groupService.getAll();
        modelMap.addAttribute("groups",groups);
        return "registration";
    }

    @GetMapping(value = "/exit")
    public String exit(HttpSession session){
        session.removeAttribute("user");
        return "authorization";
    }

    @PostMapping(value = "/signIn")
    public String resolveMainPage(@Valid User userFromClient, ModelMap modelMap, HttpSession session){
        User user = userService.checkAuthorization(userFromClient);
        if(user.isAuthorized()){
            session.setAttribute("user",user);
            UserRoles role = user.getRole().getName();
            if(role == UserRoles.STUDENT){
                return "redirect:/student/mainStudent";
            }
            if(role == UserRoles.TUTOR){
                return "redirect:/tutor/mainTutor";
            }
            if(role == UserRoles.ADMINISTRATOR){
                return "redirect:/administrator/mainAdministrator";
            }
        }else{
            modelMap.addAttribute("error",true);
        }
        return "authorization";
    };

    @PostMapping(value = "/signUp")
    public String signUp(@Valid User userFromClient,ModelMap modelMap){
        List<Group> groups = groupService.getAll();
        modelMap.addAttribute("groups",groups);
        User userFromDb = userService.findByLogin(userFromClient);

        //check if such user exists
        if(userFromDb != null){
            modelMap.addAttribute("error",true);
        }else{
            modelMap.addAttribute("success",true);
        }

        //after registration process show registration form with registration result
        return "registration";
    }
}
