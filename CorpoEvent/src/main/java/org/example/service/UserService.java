package org.example.service;

import org.example.model.Admin;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        logger.info("Saving user: {} of type {}", user.getEmail(), user.getClass().getSimpleName());
        User savedUser = userRepository.save(user);
        logger.info("Saved user: {} of type {}, isAdmin: {}", 
            savedUser.getEmail(), savedUser.getClass().getSimpleName(), savedUser instanceof Admin);
        return savedUser;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            logger.info("Found user by email {}: type = {}, isAdmin = {}", 
                email, user.getClass().getSimpleName(), user instanceof Admin);
        } else {
            logger.info("No user found with email: {}", email);
        }
        return user;
    }

    public User findById(String uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        logger.info("Found user by id {}: type = {}, isAdmin = {}", 
            uuid, user.getClass().getSimpleName(), user instanceof Admin);
        return user;
    }

    public boolean isAdmin(User user) {
        boolean isAdmin = user instanceof Admin;
        logger.info("Checking if user {} is admin: {}", user.getEmail(), isAdmin);
        return isAdmin;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            logger.info("Loaded user: {} of type {}, isAdmin: {}", 
                user.getEmail(), user.getClass().getSimpleName(), user instanceof Admin);
        }
        return users;
    }

    public void deleteUser(String uuid) {
        logger.info("Deleting user with id: {}", uuid);
        userRepository.deleteById(uuid);
    }
} 