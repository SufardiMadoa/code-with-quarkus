package org.apptest.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.apptest.entity.Guest;
import org.apptest.repository.GuestRepository;

@ApplicationScoped
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest findByCode(String code) {
        return guestRepository.findByCode(code).orElse(null);
    }
}
