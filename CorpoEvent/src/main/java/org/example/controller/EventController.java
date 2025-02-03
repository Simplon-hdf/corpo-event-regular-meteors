package org.example.controller;

import org.example.model.Event;
import org.example.model.User;
import org.example.model.Admin;
import org.example.security.SecurityUtils;
import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public String listEvents(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        boolean isAdmin = currentUser instanceof Admin;
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("canCreateEvent", SecurityUtils.canCreateEvent(currentUser));
        return "events/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (!SecurityUtils.canCreateEvent(currentUser)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de créer un événement.");
            return "redirect:/events";
        }

        Event event = new Event("", "", LocalDateTime.now(), LocalDateTime.now().plusHours(1), currentUser);
        model.addAttribute("event", event);
        model.addAttribute("currentUser", currentUser);
        return "events/create";
    }

    @PostMapping("/create")
    public String createEvent(@Valid @ModelAttribute Event event, BindingResult result, 
                            HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (!SecurityUtils.canCreateEvent(currentUser)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de créer un événement.");
            return "redirect:/events";
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", currentUser);
            return "events/create";
        }
        
        event.setCreatedBy(currentUser);
        eventService.createEvent(event);
        redirectAttributes.addFlashAttribute("success", "L'événement a été créé avec succès.");
        return "redirect:/events";
    }

    @GetMapping("/{uuid}")
    public String showEvent(@PathVariable String uuid, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        Event event = eventService.getEvent(uuid);
        
        model.addAttribute("event", event);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("canModify", SecurityUtils.canModifyEvent(currentUser, event));
        model.addAttribute("canDelete", SecurityUtils.canDeleteEvent(currentUser, event));
        model.addAttribute("canComment", SecurityUtils.canAddComment(currentUser));
        return "events/show";
    }

    @PostMapping("/{uuid}/delete")
    public String deleteEvent(@PathVariable String uuid, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        Event event = eventService.getEvent(uuid);

        if (!SecurityUtils.canDeleteEvent(currentUser, event)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de supprimer cet événement.");
            return "redirect:/events/" + uuid;
        }

        eventService.deleteEvent(uuid);
        redirectAttributes.addFlashAttribute("success", "L'événement a été supprimé avec succès.");
        return "redirect:/events";
    }

    @GetMapping("/{uuid}/edit")
    public String showEditForm(@PathVariable String uuid, Model model, HttpSession session, 
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        Event event = eventService.getEvent(uuid);

        if (!SecurityUtils.canModifyEvent(currentUser, event)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de modifier cet événement.");
            return "redirect:/events/" + uuid;
        }

        model.addAttribute("event", event);
        model.addAttribute("currentUser", currentUser);
        return "events/edit";
    }

    @PostMapping("/{uuid}/edit")
    public String updateEvent(@PathVariable String uuid, @Valid @ModelAttribute Event event,
                            BindingResult result, HttpSession session, Model model,
                            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        Event existingEvent = eventService.getEvent(uuid);

        if (!SecurityUtils.canModifyEvent(currentUser, existingEvent)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de modifier cet événement.");
            return "redirect:/events/" + uuid;
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", currentUser);
            return "events/edit";
        }

        event.setUuid(uuid);
        event.setCreatedBy(existingEvent.getCreatedBy());
        eventService.updateEvent(event);
        redirectAttributes.addFlashAttribute("success", "L'événement a été modifié avec succès.");
        return "redirect:/events/" + uuid;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 