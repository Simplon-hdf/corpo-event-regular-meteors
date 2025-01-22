package org.example;

import java.time.LocalDateTime;

public class Comment {
    private int id;
    private String content;
    private User author;
    private Event event;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(int id, String content, User author, Event event) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.event = event;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getContent() { return content; }
    public User getAuthor() { return author; }
    public Event getEvent() { return event; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
} 