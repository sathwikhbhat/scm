package com.sathwikhbhat.scm.service;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.helpers.ResourceNotFoundException;
import com.sathwikhbhat.scm.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Transactional
@Service
@Slf4j
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Contacts getContactById(String id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with ID: " + id));
    }

    public void saveContact(Contacts contact) {
        String id = UUID.randomUUID().toString();
        contact.setId(id);
        contactRepository.save(contact);
    }

    public void deleteContactById(String id) {
        try {
            contactRepository.deleteContactsById(id);
            log.info("Deleted Contact with ID: " + id);
        } catch (Exception e) {
            log.error("Error deleting contact with ID: " + id, e);
            throw new RuntimeException(e);
        }
    }

    public Contacts updateContact(Contacts contact) {
        return contactRepository.save(contact);
    }

    public Page<Contacts> getAllContactsByUser(User user, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return contactRepository.findAllByUser(user, PageRequest.of(page, size, sort));
    }

    public Page<Contacts> searchContacts(User user, String query, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepository.findByUserAndNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                user, query, query, query, pageable);
    }

}