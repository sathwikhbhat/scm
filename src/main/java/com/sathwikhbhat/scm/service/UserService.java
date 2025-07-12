package com.sathwikhbhat.scm.service;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.helpers.ResourceNotFoundException;
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

    public User saveUser(User user) {
        log.info("Saving user: {}", user);
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User getUserById(String userId) {
        log.info("Fetching user with ID: {}", userId);
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User updateUser(User updatedUser) {
        User user = userRepository.findById(updatedUser.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + updatedUser.getUserId()));
        log.info("Updating user: {}", updatedUser);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setAbout(updatedUser.getAbout());
        user.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
        user.setEnabled(updatedUser.isEnabled());
        user.setEmailVerified(updatedUser.isEmailVerified());
        user.setPhoneVerified(updatedUser.isPhoneVerified());
        user.setProvider(updatedUser.getProvider());
        user.setProviderId(updatedUser.getProviderId());

        return userRepository.save(user);

    }

    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

    public boolean userExists(String userId) {
        log.info("Checking if user exists with ID: {}", userId);
        return userRepository.existsById(userId);
    }

    public boolean userExistsByEmail(String email) {
        log.info("Checking if user exists with email: {}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

}