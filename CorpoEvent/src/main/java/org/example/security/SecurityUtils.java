package org.example.security;

import org.example.model.Admin;
import org.example.model.Collaborator;
import org.example.model.User;
import org.example.model.Event;
import org.example.model.Comment;
import org.springframework.stereotype.Component;

@Component("securityUtils")
public class SecurityUtils {

    public static boolean canCreateEvent(User user) {
        return user instanceof Admin || user instanceof Collaborator;
    }

    public static boolean canModifyEvent(User user, Event event) {
        if (user == null || event == null) {
            return false;
        }
        // Un admin peut tout modifier
        if (user instanceof Admin) {
            return true;
        }
        // Un collaborateur peut modifier ses propres événements
        return user instanceof Collaborator && event.getCreatedBy().equals(user);
    }

    public static boolean canDeleteEvent(User user, Event event) {
        return user instanceof Admin;
    }

    public static boolean canAddComment(User user) {
        return user != null;
    }

    public static boolean canDeleteComment(User user, Comment comment) {
        if (user == null || comment == null) {
            return false;
        }
        // Un admin peut supprimer n'importe quel commentaire
        if (user instanceof Admin) {
            return true;
        }
        // Un utilisateur peut supprimer ses propres commentaires
        return comment.getAuthor().getUuid().equals(user.getUuid());
    }

    public static boolean canEditComment(User user, Comment comment) {
        if (user == null || comment == null) {
            return false;
        }
        // Un admin peut modifier n'importe quel commentaire
        if (user instanceof Admin) {
            return true;
        }
        // Un utilisateur peut modifier ses propres commentaires
        return comment.getAuthor().getUuid().equals(user.getUuid());
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

    public static boolean canManageUsers(User user) {
        return user instanceof Admin;
    }

    public static boolean canCreateUser(User user) {
        return user instanceof Admin;
    }

    public static boolean canEditUser(User user, User targetUser) {
        if (user == null || targetUser == null) {
            return false;
        }
        // Un admin peut modifier n'importe quel utilisateur
        return user instanceof Admin;
    }

    public static boolean canDeleteUser(User user, User targetUser) {
        if (user == null || targetUser == null) {
            return false;
        }
        // Un admin peut supprimer n'importe quel utilisateur sauf lui-même
        return user instanceof Admin && !user.equals(targetUser);
    }
} 