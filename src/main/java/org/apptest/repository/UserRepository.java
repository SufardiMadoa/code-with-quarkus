package org.apptest.repository;
import java.util.Optional;
import java.util.UUID;

import org.apptest.entity.User;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID> {

    @Inject
    EntityManager em;

    @Transactional
    public void save(User user) {
        em.persist(user);
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                          .setParameter("email", email)
                          .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByVerificationToken(String token) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.verificationToken = :token", User.class)
                          .setParameter("token", token)
                          .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // public Optional<User> findById(UUID id) {
    //     User user = em.find(User.class, id);
    //     return Optional.ofNullable(user);
    // }

    @Transactional
    public void update(User user) {
        em.merge(user);
    }

    // public Object find(String string, String email) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'find'");
    // }
    
}