package org.apptest.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "guests")
public class Guest extends PanacheEntityBase {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    public UUID id = UUID.randomUUID();

    @Column(name = "code", nullable = false, unique = true, length = 64)
    public String code;

    @Column(name = "name", nullable = false, length = 150)
    public String name;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();
}
