package ru.nsu.sports.complex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.sports.complex.backend.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
