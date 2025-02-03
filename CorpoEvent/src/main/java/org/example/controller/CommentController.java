package org.example.controller;

import org.example.model.Comment;
import org.example.model.Event;
import org.example.model.User;
import org.example.security.SecurityUtils;
import org.example.service.CommentService;
import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/events/{eventId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventService eventService;

    @PostMapping
    public String addComment(@PathVariable String eventId,
                           @RequestParam String content,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        Event event = eventService.getEvent(eventId);

        if (!SecurityUtils.canAddComment(currentUser)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission d'ajouter un commentaire.");
            return "redirect:/events/" + eventId;
        }

        Comment comment = new Comment(UUID.randomUUID().toString(), content, currentUser, event);
        commentService.createComment(comment);
        redirectAttributes.addFlashAttribute("success", "Votre commentaire a été ajouté avec succès.");
        
        return "redirect:/events/" + eventId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable String eventId,
                              @PathVariable String commentId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        Comment comment = commentService.getComment(commentId);

        if (!SecurityUtils.canDeleteComment(currentUser)) {
            redirectAttributes.addFlashAttribute("error", "Vous n'avez pas la permission de supprimer ce commentaire.");
            return "redirect:/events/" + eventId;
        }

        commentService.deleteComment(commentId);
        redirectAttributes.addFlashAttribute("success", "Le commentaire a été supprimé avec succès.");
        
        return "redirect:/events/" + eventId;
    }
} 