package com.project.decider.controllers;

import com.project.decider.user.User;
import com.project.decider.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lobster on 30.04.16.
 */

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    UserService userService;

    @RequestMapping(value ="/", method = RequestMethod.GET)
    public String showWelcomePage() {
        return "index";
    }

    @RequestMapping(value = "/get_user", method = RequestMethod.GET)
    @ResponseBody
    public String getUser(){
        return "user";
    }
}
