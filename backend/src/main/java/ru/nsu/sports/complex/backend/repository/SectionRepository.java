package ru.nsu.sports.complex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sports.complex.backend.model.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
    Section findByName(String name);
}
