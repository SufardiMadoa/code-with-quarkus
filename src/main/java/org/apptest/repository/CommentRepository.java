package org.apptest.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apptest.entity.Comment;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {
}
