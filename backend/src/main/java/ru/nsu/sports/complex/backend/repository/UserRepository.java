package ru.nsu.sports.complex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sports.complex.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    User findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
