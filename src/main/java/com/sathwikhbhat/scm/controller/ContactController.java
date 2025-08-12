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
import org.springframework.web.bind.annotation.PathVariable;
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
    public String addContactView(Model model, Principal principal) {
        String email = Helper.getEmailOfLoggedInUser(principal);
        User loggedInUser = userService.getUserByEmail(email);
        if(!loggedInUser.isEnabled()) {
            log.warn("User {} is not enabled, redirecting to verify email page", email);
            return "user/verify-email";
        }

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

    @GetMapping("/update/{id}")
    public String editContactView(@PathVariable String id, Model model) {
        Contacts contact = contactService.getContactById(id);
        if (contact == null) {
            model.addAttribute("message", Message.builder()
                    .content("Contact not found.")
                    .type(MessageType.ERROR)
                    .build());
            return "redirect:/user/contacts/all";
        }

        ContactForm contactForm = ContactForm.builder()
                .name(contact.getName())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .address(contact.getAddress())
                .websiteLink(contact.getWebsiteLink())
                .linkedinLink(contact.getLinkedinLink())
                .favourite(contact.isFavourite())
                .description(contact.getDescription())
                .build();
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", id);
        return "user/edit-contacts";
    }

    @PostMapping("/edit/{id}")
    public String editContact(@PathVariable String id, @Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult, HttpSession session, Principal principal) {
        log.info("Updating contact with ID: {}", id);
        if (rBindingResult.hasErrors()) {
            log.error("Error in contact form: {}", rBindingResult.getAllErrors());
            session.setAttribute("message", Message.builder()
                    .content("Please correct the errors: ")
                    .type(MessageType.ERROR)
                    .build());
            return "user/edit-contacts";
        }
        try {
            Contacts contact = contactService.getContactById(id);
            if (contact == null) {
                session.setAttribute("message", Message.builder()
                        .content("Contact not found.")
                        .type(MessageType.ERROR)
                        .build());
                return "user/edit-contacts";
            }
            contact.setName(contactForm.getName());
            contact.setEmail(contactForm.getEmail());
            contact.setFavourite(contactForm.isFavourite());
            contact.setPhoneNumber(contactForm.getPhoneNumber());
            contact.setAddress(contactForm.getAddress());
            contact.setDescription(contactForm.getDescription());
            contact.setWebsiteLink(contactForm.getWebsiteLink());
            contact.setLinkedinLink(contactForm.getLinkedinLink());
            // Update image only if a new one is provided
            if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
                contact.setPictureUrl(imageService.uploadImage(contactForm.getContactImage()));
            }

            contactService.updateContact(contact);
        } catch (Exception e) {
            log.error("Error updating contact: {}", e.getMessage());
            session.setAttribute("message", Message.builder()
                    .content("Failed to update contact: " + contactForm.getName())
                    .type(MessageType.ERROR)
                    .build());
            return "user/edit-contacts";
        }

        session.setAttribute("message", Message.builder()
                .content("Successfully updated contact: " + contactForm.getName())
                .type(MessageType.SUCCESS)
                .build());
        return "redirect:/user/contacts/all?openModal=" + id;
    }

    @GetMapping("/all")
    public String allContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                              @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                              @RequestParam(value = "query", required = false) String query,
                              Model model, Principal principal) {
        String email = Helper.getEmailOfLoggedInUser(principal);
        User loggedInUser = userService.getUserByEmail(email);
        if(!loggedInUser.isEnabled()) {
            log.warn("User {} is not enabled, redirecting to verify email page", email);
            return "user/verify-email";
        }

        log.info("Fetching all contacts");

        Page<Contacts> contacts;

        if (query != null && !query.trim().isEmpty()) {
            contacts = contactService.searchContacts(loggedInUser, query.trim(), page, size, sortBy, sortDir);
        } else {
            contacts = contactService.getAllContactsByUser(loggedInUser, page, size, sortBy, sortDir);
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