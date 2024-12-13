package ru.nsu.sports.complex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sports.complex.backend.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByName(String name);
}
