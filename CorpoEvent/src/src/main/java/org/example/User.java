package org.example;

public class User {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(String uuid, String firstName, String lastName, String email, String password) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public void logout() {
        // La logique de déconnexion serait gérée par un service d'authentification
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!login(oldPassword)) {
            throw new IllegalArgumentException("L'ancien mot de passe est incorrect");
        }
        this.password = newPassword;
    }

    // Getters and Setters
    public String getUuid() { return uuid; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        this.firstName = firstName;
    }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.lastName = lastName;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
} 