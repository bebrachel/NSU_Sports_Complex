package ru.nsu.sports.complex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.sports.complex.backend.model.Section;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    Section findByName(String name);
    boolean deleteByName(String name);
}
