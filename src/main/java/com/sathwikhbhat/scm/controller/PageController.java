package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.enums.Providers;
import com.sathwikhbhat.scm.forms.UserForm;
import com.sathwikhbhat.scm.helpers.Message;
import com.sathwikhbhat.scm.helpers.MessageType;
import com.sathwikhbhat.scm.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Transactional
@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @RequestMapping
    public String index() {
        log.info("Index page");
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home() {
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
    public String registerUserPage(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) {
        log.info("Registering user: {}", userForm.getName());
        if (rBindingResult.hasErrors()) {
            log.error("Validation errors: {}", rBindingResult.getAllErrors());
            return "signup";
        }
        if (userService.userExistsByEmail(userForm.getEmail())) {
            log.error("Duplicate User: {}", userForm.getEmail());
            session.setAttribute("message", Message.builder()
                    .content("Email I'd " + userForm.getEmail() + " already registered with us. Please try with a different email.")
                    .type(MessageType.WARNING)
                    .build());
            return "redirect:/signup?error";
        }
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
            session.setAttribute("message", Message.builder()
                    .content("Failed to register user: " + userForm.getName())
                    .type(MessageType.ERROR)
                    .build());
            return "redirect:/signup?error";
        }

        session.setAttribute("message", Message.builder()
                .content("Registration successful! Please check your email to verify your account.")
                .type(MessageType.SUCCESS)
                .build());

        return "redirect:/signup";
    }

}