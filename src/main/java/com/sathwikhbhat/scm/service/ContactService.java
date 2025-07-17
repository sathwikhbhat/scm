package com.sathwikhbhat.scm.service;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.entity.User;
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
        try {
            return contactRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.error("Error retrieving contact with ID: {}", id, e);
            return null;
        }
    }

    public void saveContact(Contacts contact) {
        String id = UUID.randomUUID().toString();
        contact.setId(id);
        try {
            contactRepository.save(contact);
            log.info("Saved Contact with ID: {}", id);
        } catch (Exception e) {
            log.error("Error saving contact with ID: {}", id, e);
        }
    }

    public void deleteContactById(String id) {
        try {
            contactRepository.deleteContactsById(id);
            log.info("Deleted Contact with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting contact with ID: {}", id, e);
        }
    }

    public Contacts updateContact(Contacts contact) {
        try {
            contactRepository.save(contact);
            log.info("Updated Contact with ID: {}", contact.getId());
            return contact;
        } catch (Exception e) {
            log.error("Error saving contact with ID: {}", contact.getId(), e);
        }
        return null;
    }

    public Page<Contacts> getAllContactsByUser(User user, int page, int size, String sortBy, String sortDir) {
        try {
            log.info("Fetching all contacts for user: {}", user.getUserId());
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            return contactRepository.findAllByUser(user, PageRequest.of(page, size, sort));
        } catch (Exception e) {
            log.error("Error fetching contacts for user: {}", user.getUserId(), e);
            return null;
        }
    }

    public Page<Contacts> searchContacts(User user, String query, int page, int size, String sortBy, String sortDir) {
        try {
            log.info("Searching contacts for user: {} with query: {}", user.getUserId(), query);
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);

            return contactRepository.findByUserAndNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                    user, query, query, query, pageable);
        } catch (Exception e) {
            log.error("Error fetching contacts for user: {} with query: {}", user.getUserId(), query, e);
            return null;
        }
    }

}