package org.apptest.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    public UUID id;

    @Column(unique = true, nullable = false)
    public String email;

    @Column(name= "password_hash", nullable=false)
    public String passwordHash;
    
    @Column(name= "name", nullable=false)
    public String name;
    @Column(name= "username", nullable=false)
    public String username;

    @Column(nullable=false)
    public String status; // UNVERIFIED, VERIFIED, APPROVED
    public String verificationToken;
    public LocalDateTime createAt = LocalDateTime.now();

}
