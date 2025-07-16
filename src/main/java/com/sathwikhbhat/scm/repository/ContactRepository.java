package com.sathwikhbhat.scm.repository;

import com.sathwikhbhat.scm.entity.Contacts;
import com.sathwikhbhat.scm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, String> {

    Page<Contacts> findAllByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Contacts c WHERE c.user.id = :userId")
    List<Contacts> findByUserId(@Param("userId") String userId);

    Page<Contacts> findByUserAndNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            User user, String name, String email, String phone, Pageable pageable);

    void deleteContactsById(String id);

}