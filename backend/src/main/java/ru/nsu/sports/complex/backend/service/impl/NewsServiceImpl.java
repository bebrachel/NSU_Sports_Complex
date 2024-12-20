package ru.nsu.sports.complex.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.model.News;
import ru.nsu.sports.complex.backend.repository.NewsRepository;
import ru.nsu.sports.complex.backend.service.NewsService;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);
    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public News create(News news) {
        return newsRepository.save(news);
    }

    @Override
    public News find(Integer id) {
        return newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("News not found with id: " + id));
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAll();
    }

    @Override
    public News update(Integer id, News news) {
        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException("News not found with id: " + id);
        }
        news.setId(id);
        return newsRepository.save(news);
    }

    @Override
    public boolean delete(Integer id) {
        try {
            newsRepository.deleteById(id);
            return true;
        } catch (DataAccessException e) {
            LOGGER.error("Error deleting news with id {}: {}", id, e.getMessage());
            return false;
        }
    }
}
