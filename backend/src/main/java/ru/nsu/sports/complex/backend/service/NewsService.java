package ru.nsu.sports.complex.backend.service;

import ru.nsu.sports.complex.backend.model.News;

import java.util.List;

public interface NewsService {
    News find(Integer id);
    List<News> findAll();
    News create(News news);
    News update(Integer id, News news);
    boolean delete(Integer id);
}
