package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String uuid;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User createdBy;
    private List<Comment> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Event(String uuid, String title, String description, LocalDateTime startDate, 
                LocalDateTime endDate, User createdBy) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.comments = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        this.updatedAt = LocalDateTime.now();
    }

    public void modifyComment(Comment comment) {
        int index = comments.indexOf(comment);
        if (index != -1) {
            comments.set(index, comment);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean isOwner(User user) {
        return this.createdBy.equals(user);
    }

    // Getters and Setters
    public String getUuid() { return uuid; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) {
        if (startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin");
        }
        this.startDate = startDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) {
        if (endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début");
        }
        this.endDate = endDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public User getCreatedBy() { return createdBy; }
    public List<Comment> getComments() { return new ArrayList<>(comments); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
} 