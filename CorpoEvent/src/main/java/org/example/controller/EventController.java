package org.example.controller;

import org.example.model.Event;
import org.example.model.User;
import org.example.security.SecurityUtils;
import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @GetMapping
    public String listEvents(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("canCreateEvent", SecurityUtils.canCreateEvent(currentUser));
        return "events/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (!SecurityUtils.canCreateEvent(currentUser)) {
            logger.warn("Unauthorized access attempt to create event form by user: {}", currentUser.getEmail());
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
        logger.info("Event creation attempt by user: {}", currentUser.getEmail());

        if (result.hasErrors()) {
            logger.warn("Form validation failed for event creation by user: {}", currentUser.getEmail());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("error", "Veuillez corriger les erreurs dans le formulaire.");
            return "events/create";
        }

        if (!SecurityUtils.canCreateEvent(currentUser)) {
            logger.warn("Permission denied for event creation to user: {}", currentUser.getEmail());
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de créer un événement.");
            return "redirect:/events";
        }

        try {
            event.setCreatedBy(currentUser);
            Event createdEvent = eventService.createEvent(event);
            logger.info("Event created successfully: {} by user: {}", createdEvent.getUuid(), currentUser.getEmail());
            redirectAttributes.addFlashAttribute("success", "L'événement a été créé avec succès.");
            return "redirect:/events";

        } catch (IllegalArgumentException e) {
            logger.error("Validation error during event creation: {}", e.getMessage());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("error", e.getMessage());
            return "events/create";

        } catch (Exception e) {
            logger.error("Unexpected error during event creation: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Une erreur inattendue est survenue. Veuillez réessayer.");
            return "redirect:/events";
        }
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

        try {
            eventService.deleteEvent(uuid);
            redirectAttributes.addFlashAttribute("success", "L'événement a été supprimé avec succès.");
            return "redirect:/events";
        } catch (Exception e) {
            logger.error("Error deleting event {}: {}", uuid, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la suppression de l'événement.");
            return "redirect:/events/" + uuid;
        }
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

        try {
            event.setUuid(uuid);
            event.setCreatedBy(existingEvent.getCreatedBy());
            eventService.updateEvent(event);
            redirectAttributes.addFlashAttribute("success", "L'événement a été modifié avec succès.");
            return "redirect:/events/" + uuid;
        } catch (Exception e) {
            logger.error("Error updating event {}: {}", uuid, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la modification de l'événement.");
            return "redirect:/events/" + uuid;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 