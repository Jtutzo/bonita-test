package com.example.bonitatest.user;

import com.example.bonitatest.user.User;
import jakarta.persistence.EntityManagerFactory;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class UserRepository {

    private final EntityManagerFactory entityManagerFactory;
    private long counter = 0;

    @Inject
    private UserRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Optional<User> findById(long id) {
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var user = entityManager.find(User.class, id);
        entityManager.getTransaction().commit();
        entityManager.close();
        return Optional.ofNullable(user);
    }

    public long create(User user) {
        user.setId(++counter);
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
        return user.getId();
    }

}
