package org.example;

public class Collaborator extends User {
    
    public Collaborator(String uuid, String firstName, String lastName, String email, String password) {
        super(uuid, firstName, lastName, email, password);
    }

    public Event createEvent(Event event) {
        if (!event.getCreatedBy().equals(this)) {
            throw new IllegalArgumentException("L'événement doit être créé par ce collaborateur");
        }
        return event;
    }

    public void modifyOwnEvent(Event event) {
        if (!event.isOwner(this)) {
            throw new IllegalArgumentException("Vous ne pouvez modifier que vos propres événements");
        }
    }

    public void deleteOwnEvent(Event event) {
        if (!event.isOwner(this)) {
            throw new IllegalArgumentException("Vous ne pouvez supprimer que vos propres événements");
        }
    }

    public void addComment(Comment comment, Event event) {
        if (!comment.getAuthor().equals(this)) {
            throw new IllegalArgumentException("Vous ne pouvez ajouter que des commentaires dont vous êtes l'auteur");
        }
        event.addComment(comment);
    }

    public void modifyOwnComment(Comment comment) {
        if (!comment.getAuthor().equals(this)) {
            throw new IllegalArgumentException("Vous ne pouvez modifier que vos propres commentaires");
        }
        comment.getEvent().modifyComment(comment);
    }

    public void deleteOwnComment(Comment comment) {
        if (!comment.getAuthor().equals(this)) {
            throw new IllegalArgumentException("Vous ne pouvez supprimer que vos propres commentaires");
        }
        comment.getEvent().removeComment(comment);
    }

    public void deleteCommentFromOwnEvent(Comment comment, Event event) {
        if (!event.isOwner(this)) {
            throw new IllegalArgumentException("Vous ne pouvez supprimer des commentaires que de vos propres événements");
        }
        event.removeComment(comment);
    }
} 