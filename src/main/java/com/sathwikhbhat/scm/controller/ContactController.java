package com.sathwikhbhat.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sathwikhbhat.scm.forms.ContactForm;
import com.sathwikhbhat.scm.helpers.Message;
import com.sathwikhbhat.scm.helpers.MessageType;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @GetMapping("/add")
    public String addContactView(Model model) {
        model.addAttribute("contactForm", new ContactForm());

        return "user/add-contacts";
    }

    @PostMapping("/save")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult, HttpSession session) {
        log.info("Saving contact: {}", contactForm.getName());
        if (rBindingResult.hasErrors()) {
            log.error("Error in contact form: {}", rBindingResult.getAllErrors());
            return "user/add-contacts";
        }
        // try {

        // }

        session.setAttribute("message", Message.builder()
                .content("Successfully added contact: " + contactForm.getName())
                .type(MessageType.SUCCESS)
                .build());
        return "redirect:user/add-contacts";
    }

}
