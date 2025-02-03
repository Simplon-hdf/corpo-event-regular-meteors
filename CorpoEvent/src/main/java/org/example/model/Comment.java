package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter @Setter @NoArgsConstructor
public class Comment {
    @Id
    private String uuid;

    @NotBlank(message = "Le contenu ne peut pas être vide")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @NotNull(message = "L'auteur est obligatoire")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    @NotNull(message = "L'événement est obligatoire")
    private Event event;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(String uuid, String content, User author, Event event) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu ne peut pas être vide");
        }
        if (event == null) {
            throw new IllegalArgumentException("L'événement est obligatoire");
        }
        this.uuid = uuid;
        this.content = content;
        this.author = author;
        this.event = event;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setEvent(Event event) {
        if (this.event != null) {
            this.event.getComments().remove(this);
        }
        this.event = event;
        if (event != null) {
            event.getComments().add(this);
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setAuthor(User author) {
        if (this.author != null) {
            this.author.getComments().remove(this);
        }
        this.author = author;
        if (author != null) {
            author.getComments().add(this);
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Comment comment = (Comment) obj;
        return uuid.equals(comment.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
} 