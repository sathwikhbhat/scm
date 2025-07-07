package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.helpers.Helper;
import com.sathwikhbhat.scm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Slf4j
@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Principal principal) {

        if (principal == null) {
            log.warn("No principal found, user is not logged in.");
            return;
        }

        String emailOfLoggedInUser = Helper.getEmailOfLoggedInUser(principal);
        log.info("Adding logged in user to model: {}", emailOfLoggedInUser);

        User userLoggedIn = userService.getUserByEmail(emailOfLoggedInUser);
        model.addAttribute("user", userLoggedIn);
    }

}