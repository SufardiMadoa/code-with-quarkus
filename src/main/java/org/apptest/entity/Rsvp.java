package org.apptest.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rsvps")
public class Rsvp extends PanacheEntityBase {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    public UUID id = UUID.randomUUID();

    @Column(name = "guest_code", nullable = false, length = 64)
    public String guestCode;

    @Column(name = "guest_name", nullable = false, length = 150)
    public String guestName;

    @Column(name = "attendance", nullable = false, length = 20)
    public String attendance; // "hadir" / "tidak-hadir"

    @Column(name = "created_at", nullable = false)
    public Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt = Instant.now();
}
