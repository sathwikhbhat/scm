package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.helpers.Helper;
import com.sathwikhbhat.scm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/dashboard")
    public String userDashboard(Principal principal) {
        String email = Helper.getEmailOfLoggedInUser(principal);
        User loggedInUser = userService.getUserByEmail(email);
        if(!loggedInUser.isEnabled()) {
            log.warn("User {} is not enabled, redirecting to verify email page", email);
            return "user/verify-email";
        }

        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Principal principal) {
        String email = Helper.getEmailOfLoggedInUser(principal);
        User loggedInUser = userService.getUserByEmail(email);
        if(!loggedInUser.isEnabled()) {
            log.warn("User {} is not enabled, redirecting to verify email page", email);
            return "user/verify-email";
        }

        return "user/profile";
    }

}