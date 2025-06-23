package com.sathwikhbhat.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Substring Technologies");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(){
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(){
        return "services";
    }

}