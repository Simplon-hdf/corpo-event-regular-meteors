package org.example.service;

import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getComment(String uuid) {
        return commentRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(String uuid) {
        commentRepository.deleteById(uuid);
    }
} 