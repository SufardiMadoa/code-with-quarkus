package org.apptest.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.apptest.entity.Guest;
import org.apptest.repository.GuestRepository;

import java.util.List;

@ApplicationScoped
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest findByCode(String code) {
        return guestRepository.findByCode(code).orElse(null);
    }
    public List<Guest> getAllGuests() {
        // kalau GuestRepository extends PanacheRepository<Guest>
        return guestRepository.listAll();

        // Atau kalau kamu punya method khusus di repository:
        // return guestRepository.listAllSortedByCode();
    }
}
