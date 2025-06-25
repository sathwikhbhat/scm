package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.enums.Providers;
import com.sathwikhbhat.scm.forms.UserForm;
import com.sathwikhbhat.scm.helpers.Message;
import com.sathwikhbhat.scm.helpers.MessageType;
import com.sathwikhbhat.scm.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Transactional
@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Substring Technologies");
        log.info("Home page");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        log.info("About page");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        log.info("Services page");
        return "services";
    }

    @RequestMapping("/login")
    public String loginPage() {
        log.info("Login page");
        return "login";
    }

    @RequestMapping("/signup")
    public String signupPage(Model model) {
        log.info("Signup page");
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "signup";
    }

    @RequestMapping("/contact")
    public String contactPage() {
        log.info("Contact page");
        return "contact";
    }

    @PostMapping("/registerUser")
    public String registerUserPage(@ModelAttribute UserForm userForm, HttpSession session) {
        log.info("Registering user: {}", userForm.getName());
        try {
            User user = User.builder()
                    .name(userForm.getName())
                    .email(userForm.getEmail())
                    .password(userForm.getPassword())
                    .phoneNumber(userForm.getPhoneNumber())
                    .provider(Providers.SELF)
                    .profilePictureUrl("./images/default-profile-pic.png")
                    .enabled(false)
                    .emailVerified(false)
                    .phoneVerified(false)
                    .build();
            userService.saveUser(user);
            log.info("User registered successfully: {}", user.getName());
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            return "redirect:/signup?error";
        }

        session.setAttribute("message", Message.builder()
                .content("Registration successful! Please check your email to verify your account.")
                .type(MessageType.SUCCESS)
                .build());

        return "redirect:/signup";
    }

}