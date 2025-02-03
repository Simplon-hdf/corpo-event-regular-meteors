package org.example.service;

import org.example.model.Admin;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        // Force le chargement du type d'utilisateur
        if (user != null) {
            boolean isAdmin = user instanceof Admin;
            user.getClass().getName();
        }
        return user;
    }

    public User findById(String uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean isAdmin(User user) {
        return user instanceof Admin;
    }
} 