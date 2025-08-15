package com.sathwikhbhat.scm.service;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        try {
            log.info("Saving user: {}", user);
            if (user.getUserId() == null || user.getUserId().isEmpty()) {
                user.setUserId(UUID.randomUUID().toString());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRoles(List.of("USER"));
            }
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error saving user: {}", user, e);
        }
    }

    public User getUserById(String userId) {
        try {
            log.info("Fetching user with ID: {}", userId);
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", userId, e);
            return null;
        }
    }

    public User getUserByEmail(String email) {
        try {
            log.info("Fetching user with email: {}", email);
            return userRepository.findByEmail(email.toLowerCase().trim()).orElse(null);
        } catch (Exception e) {
            log.error("Error fetching user with email: {}", email, e);
            return null;
        }
    }

    public String getUserIdByEmail(String email) {
        try {
            log.info("Fetching user ID for email: {}", email);
            User user = userRepository.findByEmail(email).orElse(null);
            assert user != null;
            return user.getUserId();
        } catch (Exception e) {
            log.error("Error fetching user ID for email: {}", email, e);
            return null;
        }
    }

    public boolean userExistsByEmail(String email) {
        try {
            log.info("Checking if user exists with email: {}", email);
            return userRepository.findByEmail(email).isPresent();
        } catch (Exception e) {
            log.error("Error checking if user exists with email: {}", email, e);
            return false;
        }
    }

}