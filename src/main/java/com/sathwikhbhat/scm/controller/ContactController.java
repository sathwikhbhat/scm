package com.sathwikhbhat.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sathwikhbhat.scm.forms.ContactForm;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @GetMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/add-contacts";
    }

}
