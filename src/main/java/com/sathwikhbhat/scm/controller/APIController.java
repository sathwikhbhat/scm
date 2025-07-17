package com.sathwikhbhat.scm.controller;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Contacts> getContactById(@PathVariable String id) {
        Contacts contacts = null;
        try {
            contacts = contactService.getContactById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @DeleteMapping("/contacts/delete/{id}")
    public ResponseEntity<String> deleteContactById(@PathVariable String id) {
        try {
            contactService.deleteContactById(id);
            return new ResponseEntity<>("Contact deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting contact: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}