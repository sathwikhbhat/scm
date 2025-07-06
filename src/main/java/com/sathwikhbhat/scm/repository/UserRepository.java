package com.sathwikhbhat.scm.repository;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.enums.Providers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    String name(String name);

    String email(String email);

    boolean existsByEmailAndProviderNot(String email, Providers provider);

}