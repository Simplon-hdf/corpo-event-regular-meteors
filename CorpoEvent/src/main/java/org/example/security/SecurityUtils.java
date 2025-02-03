package org.example.security;

import org.example.model.Admin;
import org.example.model.Collaborator;
import org.example.model.User;
import org.example.model.Event;

public class SecurityUtils {

    public static boolean canCreateEvent(User user) {
        return user instanceof Admin || user instanceof Collaborator;
    }

    public static boolean canModifyEvent(User user, Event event) {
        return user instanceof Admin || 
               (user instanceof Collaborator && event.isOwner(user));
    }

    public static boolean canDeleteEvent(User user, Event event) {
        return user instanceof Admin;
    }

    public static boolean canAddComment(User user) {
        return true; // Tous les utilisateurs peuvent commenter
    }

    public static boolean canDeleteComment(User user) {
        return user instanceof Admin;
    }

    public static boolean canRateEvent(User user) {
        return true; // Tous les utilisateurs peuvent noter
    }

    public static boolean canScheduleEvent(User user) {
        return user instanceof Admin || user instanceof Collaborator;
    }

    public static boolean canShareEvent(User user) {
        return true; // Tous les utilisateurs peuvent partager
    }
} 