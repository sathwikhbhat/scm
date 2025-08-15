package com.sathwikhbhat.scm.repository;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, String> {

    Page<Contacts> findAllByUser(User user, Pageable pageable);

    Page<Contacts> findByUserAndNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            User user, String name, String email, String phone, Pageable pageable);

    void deleteContactsById(String id);

}