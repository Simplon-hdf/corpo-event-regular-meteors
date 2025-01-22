package org.example;

public class Admin extends User {
    
    public Admin(String uuid, String firstName, String lastName, String email, String password) {
        super(uuid, firstName, lastName, email, password);
    }

    public Collaborator createCollaborator(String uuid, String firstName, String lastName, String email, String password) {
        return new Collaborator(uuid, firstName, lastName, email, password);
    }

    public void modifyCollaborator(Collaborator collaborator, String firstName, String lastName, String email) {
        // La modification réelle serait gérée par un UserRepository
        if (firstName != null && !firstName.isEmpty()) {
            collaborator.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            collaborator.setLastName(lastName);
        }
        if (email != null && !email.isEmpty()) {
            collaborator.setEmail(email);
        }
    }

    public void deleteUser(User user) {
        if (user instanceof Admin) {
            throw new IllegalArgumentException("Impossible de supprimer un administrateur");
        }
        // La suppression réelle serait gérée par un UserRepository
    }

    public void modifyEvent(Event event, String title, String description) {
        if (title != null && !title.isEmpty()) {
            event.setTitle(title);
        }
        if (description != null && !description.isEmpty()) {
            event.setDescription(description);
        }
    }

    public void deleteEvent(Event event) {
        // La suppression réelle serait gérée par un EventRepository
    }

    public void deleteComment(Comment comment) {
        comment.getEvent().removeComment(comment);
    }
} 