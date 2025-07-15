package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.forms.ContactForm;
import com.sathwikhbhat.scm.helpers.Helper;
import com.sathwikhbhat.scm.helpers.Message;
import com.sathwikhbhat.scm.helpers.MessageType;
import com.sathwikhbhat.scm.service.ContactService;
import com.sathwikhbhat.scm.service.ImageService;
import com.sathwikhbhat.scm.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/add")
    public String addContactView(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "user/add-contacts";
    }

    @PostMapping("/save")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult, HttpSession session, Principal principal) {
        log.info("Saving contact: {}", contactForm.getName());
        if (rBindingResult.hasErrors()) {
            log.error("Error in contact form: {}", rBindingResult.getAllErrors());
            session.setAttribute("message", Message.builder()
                    .content("Please correct the errors: ")
                    .type(MessageType.ERROR)
                    .build());
            return "user/add-contacts";
        }
        try {
            Contacts contact = Contacts.builder()
                    .name(contactForm.getName())
                    .email(contactForm.getEmail())
                    .favourite(contactForm.isFavourite())
                    .phoneNumber(contactForm.getPhoneNumber())
                    .address(contactForm.getAddress())
                    .description(contactForm.getDescription())
                    .websiteLink(contactForm.getWebsiteLink())
                    .linkedinLink(contactForm.getLinkedinLink())
                    .pictureUrl(imageService.uploadImage(contactForm.getContactImage()))
                    .user(userService.getUserByEmail(Helper.getEmailOfLoggedInUser(principal)))
                    .build();

            log.info("Saving contact: {}", contact.getName());
            contactService.saveContact(contact);
        } catch (Exception e) {
            log.error("Error saving contact: {}", e.getMessage());
            session.setAttribute("message", Message.builder()
                    .content("Failed to add contact: " + contactForm.getName())
                    .type(MessageType.ERROR)
                    .build());
            return "user/add-contacts";
        }

        session.setAttribute("message", Message.builder()
                .content("Successfully added contact: " + contactForm.getName())
                .type(MessageType.SUCCESS)
                .build());
        return "redirect:/user/contacts/add";
    }

    @GetMapping("/all")
    public String allContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                              @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                              @RequestParam(value = "query", required = false) String query,
                              Model model, Principal principal) {
        log.info("Fetching all contacts");
        String name = Helper.getEmailOfLoggedInUser(principal);
        User user = userService.getUserByEmail(name);

        Page<Contacts> contacts;

        if (query != null && !query.trim().isEmpty()) {
            contacts = contactService.searchContacts(user, query.trim(), page, size, sortBy, sortDir);
        } else {
            contacts = contactService.getAllContactsByUser(user, page, size, sortBy, sortDir);
        }

        model.addAttribute("contacts", contacts);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        String queryParam = (query != null && !query.isEmpty()) ? "&query=" + UriUtils.encodeQueryParam(query, StandardCharsets.UTF_8) : "";
        model.addAttribute("queryParam", queryParam);


        model.addAttribute("nextSortDirForName", sortBy.equals("name") && sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("nextSortDirForEmail", sortBy.equals("email") && sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("nextSortDirForPhone", sortBy.equals("phoneNumber") && sortDir.equals("asc") ? "desc" : "asc");

        return "user/all-contacts";
    }

}