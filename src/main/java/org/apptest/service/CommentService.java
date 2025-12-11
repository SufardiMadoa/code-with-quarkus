package org.apptest.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apptest.entity.Comment;
import org.apptest.param.CommentRequest;
import org.apptest.repository.CommentRepository;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment addComment(CommentRequest request) {
        Comment c = new Comment();
        c.guestName = request.name;
        c.message = request.message;
        c.createdAt = Instant.now();
        commentRepository.persist(c);
        return c;
    }

    public List<Comment> listComments(int limit) {
        return commentRepository.findAll()
                .page(0, limit)
                .list();
    }
}
