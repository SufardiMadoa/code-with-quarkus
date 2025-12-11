package org.apptest.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apptest.entity.Guest;

import java.util.Optional;

@ApplicationScoped
public class GuestRepository implements PanacheRepository<Guest> {

    public Optional<Guest> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }
}
