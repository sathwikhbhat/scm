package com.sathwikhbhat.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public String userProfile() {
        return "user/profile";
    }

    @GetMapping("/verify-email")
    public String showVerifyEmailPage() {
        return "user/verify-email";
    }

}