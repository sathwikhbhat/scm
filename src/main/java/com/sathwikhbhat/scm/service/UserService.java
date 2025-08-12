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
            user.setUserId(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
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

    public User updateUser(User updatedUser) {
        try {
            User user = userRepository.findById(updatedUser.getUserId()).orElse(null);
            log.info("Updating user: {}", updatedUser);
            assert user != null;
            User.builder()
                    .name(updatedUser.getName())
                    .email(updatedUser.getEmail())
                    .phoneNumber(updatedUser.getPhoneNumber())
                    .profilePictureUrl(updatedUser.getProfilePictureUrl())
                    .enabled(updatedUser.isEnabled())
                    .provider(updatedUser.getProvider())
                    .providerId(updatedUser.getProviderId())
                    .build();


            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error updating user: {}", updatedUser, e);
            return null;
        }
    }

    public void deleteUser(String userId) {
        try {
            log.info("Deleting user with ID: {}", userId);
            User user = userRepository.findById(userId).orElse(null);
            assert user != null;
            userRepository.delete(user);
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", userId, e);
        }
    }

    public boolean userExists(String userId) {
        try {
            log.info("Checking if user exists with ID: {}", userId);
            return userRepository.existsById(userId);
        } catch (Exception e) {
            log.error("Error checking if user exists with ID: {}", userId, e);
            return false;
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

    public List<User> getAllUsers() {
        try {
            log.info("Fetching all users");
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            return null;
        }
    }

}