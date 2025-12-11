package org.apptest.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apptest.entity.Rsvp;
import org.apptest.param.RsvpRequest;
import org.apptest.repository.RsvpRepository;
import org.apptest.response.RsvpStatusResponse;

import java.time.Instant;

@ApplicationScoped
public class RsvpService {

    private final RsvpRepository rsvpRepository;

    public RsvpService(RsvpRepository rsvpRepository) {
        this.rsvpRepository = rsvpRepository;
    }

    public Rsvp findByGuestCode(String guestCode) {
        return rsvpRepository.findByGuestCode(guestCode).orElse(null);
    }

    @Transactional
    public Rsvp saveOrUpdate(RsvpRequest request) {
        Rsvp existing = rsvpRepository.findByGuestCode(request.guestCode).orElse(null);
        if (existing == null) {
            existing = new Rsvp();
            existing.guestCode = request.guestCode;
            existing.createdAt = Instant.now();
        }
        existing.guestName = request.guestName;
        existing.attendance = request.attendance;
        existing.updatedAt = Instant.now();

        rsvpRepository.persist(existing);
        return existing;
    }

    public RsvpStatusResponse getStats() {
        long hadir = rsvpRepository.countByAttendance("hadir");
        long tidakHadir = rsvpRepository.countByAttendance("tidak-hadir");
        return new RsvpStatusResponse(hadir, tidakHadir);
    }
}
