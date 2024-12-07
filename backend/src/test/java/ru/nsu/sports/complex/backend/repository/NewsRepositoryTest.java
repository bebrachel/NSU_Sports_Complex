package ru.nsu.sports.complex.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.sports.complex.backend.model.News;
import ru.nsu.sports.complex.backend.service.impl.NewsServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NewsRepositoryTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindNews() {
        News news = new News(
                null,
                "Title 1",
                "Content 1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Author 1");

        when(newsRepository.save(news)).thenReturn(new News(
                1, "Title 1", "Content 1",
                LocalDateTime.now(), LocalDateTime.now(), "Author 1"));

        News savedNews = newsService.create(news);

        assertNotNull(savedNews);
        assertEquals("Title 1", savedNews.getTitle());
        assertEquals("Content 1", savedNews.getContent());
        assertEquals("Author 1", savedNews.getAuthor());
    }

    @Test
    void testFindById() {
        Integer newsId = 1;
        News news = new News(
                newsId, "Title 1", "Content 1",
                LocalDateTime.now(), LocalDateTime.now(), "Author 1");

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        News foundNews = newsService.find(newsId);

        assertNotNull(foundNews);
        assertEquals(newsId, foundNews.getId());
        assertEquals("Title 1", foundNews.getTitle());
        assertEquals("Content 1", foundNews.getContent());
        assertEquals("Author 1", foundNews.getAuthor());
    }
}
