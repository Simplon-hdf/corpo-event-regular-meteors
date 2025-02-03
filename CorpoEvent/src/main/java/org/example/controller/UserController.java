package org.example.controller;

import org.example.model.Admin;
import org.example.model.Collaborator;
import org.example.model.User;
import org.example.security.SecurityUtils;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (!SecurityUtils.canManageUsers(currentUser)) {
            return "redirect:/events";
        }
        
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", currentUser);
        return "users/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (!SecurityUtils.canCreateUser(currentUser)) {
            return "redirect:/events";
        }
        
        model.addAttribute("currentUser", currentUser);
        return "users/create";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String userType,
                           HttpSession session, 
                           RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (!SecurityUtils.canCreateUser(currentUser)) {
            return "redirect:/events";
        }

        String uuid = UUID.randomUUID().toString();
        User newUser;
        String defaultPassword = "defaultPassword123";
        
        if ("ADMIN".equals(userType)) {
            newUser = new Admin(uuid, firstName, lastName, email, defaultPassword);
        } else {
            newUser = new Collaborator(uuid, firstName, lastName, email, defaultPassword);
        }

        userService.save(newUser);
        redirectAttributes.addFlashAttribute("success", "Utilisateur créé avec succès");
        return "redirect:/admin/users";
    }

    @GetMapping("/{uuid}/edit")
    public String showEditForm(@PathVariable String uuid, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        User targetUser = userService.findById(uuid);
        
        if (!SecurityUtils.canEditUser(currentUser, targetUser)) {
            return "redirect:/events";
        }

        model.addAttribute("user", targetUser);
        model.addAttribute("currentUser", currentUser);
        return "users/edit";
    }

    @PostMapping("/{uuid}/edit")
    public String updateUser(@PathVariable String uuid, 
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           HttpSession session, 
                           RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        User existingUser = userService.findById(uuid);
        
        if (!SecurityUtils.canEditUser(currentUser, existingUser)) {
            return "redirect:/events";
        }

        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setEmail(email);

        userService.save(existingUser);
        redirectAttributes.addFlashAttribute("success", "Utilisateur modifié avec succès");
        return "redirect:/admin/users";
    }

    @PostMapping("/{uuid}/delete")
    public String deleteUser(@PathVariable String uuid, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        User targetUser = userService.findById(uuid);
        
        if (!SecurityUtils.canDeleteUser(currentUser, targetUser)) {
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas supprimer cet utilisateur");
            return "redirect:/admin/users";
        }

        userService.deleteUser(uuid);
        redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé avec succès");
        return "redirect:/admin/users";
    }
} 