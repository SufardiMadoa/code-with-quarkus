package org.apptest.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment extends PanacheEntityBase {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    public UUID id = UUID.randomUUID();

    @Column(name = "guest_name", nullable = false, length = 150)
    public String guestName;

    @Column(name = "message", nullable = false, columnDefinition = "text")
    public String message;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();
}
