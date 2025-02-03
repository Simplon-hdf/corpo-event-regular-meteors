package org.example.controller;

import org.example.model.Admin;
import org.example.model.Collaborator;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        Admin admin = new Admin(UUID.randomUUID().toString(), "Jean", "Dupont", "admin@corpoevent.fr", "password");
        logger.info("Creating admin user: {} of type {}", admin.getEmail(), admin.getClass().getSimpleName());
        createUserIfNotExists(admin);

        createUserIfNotExists(
            new Collaborator(UUID.randomUUID().toString(), "Marie", "Martin", "collab1@corpoevent.fr", "password")
        );
        createUserIfNotExists(
            new Collaborator(UUID.randomUUID().toString(), "Pierre", "Bernard", "collab2@corpoevent.fr", "password")
        );
    }

    private void createUserIfNotExists(User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            User savedUser = userService.save(user);
            logger.info("Saved user: {} of type {}", savedUser.getEmail(), savedUser.getClass().getSimpleName());
        }
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            return "redirect:/events";
        }
        
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "index";
    }

    @GetMapping("/login/{index}")
    public String login(@PathVariable int index, HttpSession session) {
        List<User> users = userService.getAllUsers();
        
        if (index >= 0 && index < users.size()) {
            User user = users.get(index);
            boolean isAdmin = user instanceof Admin;
            logger.info("User logging in: {} of type {}, isAdmin: {}", user.getEmail(), user.getClass().getSimpleName(), isAdmin);
            session.setAttribute("currentUser", user);
            session.setMaxInactiveInterval(1800);
            return "redirect:/events";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 