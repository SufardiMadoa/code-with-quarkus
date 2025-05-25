package org.apptest.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apptest.entity.Login;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginRepository implements PanacheRepository<Login> {

    public Optional<Login> findByToken(String token) {
        return find("token", token).firstResultOptional();
    }

    public List<Login> findAllByUserId(Long userId) {
        return list("userId", userId);
    }

    public void deleteByExpiryBefore(Timestamp timestamp) {
        delete("expiry < ?1", timestamp);
    }
}
