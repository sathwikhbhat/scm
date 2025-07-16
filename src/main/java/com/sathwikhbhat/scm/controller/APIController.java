package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{id}")
    public Contacts getContactById(@PathVariable String id) {
        return contactService.getContactById(id);
    }

    @DeleteMapping("/contacts/delete/{id}")
    public void deleteContactById(@PathVariable String id) {
         contactService.deleteContactById(id);
    }

}