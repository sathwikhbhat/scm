package com.sathwikhbhat.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.service.UserService;

@Controller
@RequestMapping("/verification")
public class VerificationController {

    @Autowired
    UserService userService;

    @GetMapping
    public String verificationPage(@RequestParam String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return "redirect:/error";
        }
        user.setEnabled(true);
        userService.saveUser(user);
        return "user/email-verified";
    }

}