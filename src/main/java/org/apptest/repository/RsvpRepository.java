package org.apptest.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apptest.entity.Rsvp;

import java.util.Optional;

@ApplicationScoped
public class RsvpRepository implements PanacheRepository<Rsvp> {

    public Optional<Rsvp> findByGuestCode(String guestCode) {
        return find("guestCode", guestCode).firstResultOptional();
    }

    public long countByAttendance(String attendance) {
        return count("attendance", attendance);
    }
}
