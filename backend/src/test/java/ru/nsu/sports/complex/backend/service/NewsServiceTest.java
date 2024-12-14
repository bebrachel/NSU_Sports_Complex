package ru.nsu.sports.complex.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.sports.complex.backend.model.News;
import ru.nsu.sports.complex.backend.repository.NewsRepository;
import ru.nsu.sports.complex.backend.service.impl.NewsServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNews() {
        News news = new News(null, "Title 1", "Content 1", null, null, "Joe Biden");
        when(newsRepository.save(news)).thenReturn(new News(1, "Title 1", "Content 1", null, null, "Joe Biden"));

        News createdNews = newsService.create(news);

        assertNotNull(createdNews.getId());
        assertEquals("Title 1", createdNews.getTitle());
        assertEquals("Content 1", createdNews.getContent());
        assertEquals("Joe Biden", createdNews.getAuthor());
        verify(newsRepository, times(1)).save(news);
    }

    @Test
    void testFindNewsById() {
        Integer newsId = 1;
        News news = new News(newsId, "Title 1", "Content 1", null, null, "Joe Biden");

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        News result = newsService.find(newsId);

        assertNotNull(result);
        assertEquals(newsId, result.getId());
        assertEquals("Title 1", result.getTitle());
        assertEquals("Content 1", result.getContent());
        assertEquals("Joe Biden", result.getAuthor());
        verify(newsRepository, times(1)).findById(newsId);
    }

    @Test
    void testFindAllNews() {
        List<News> newsList = Arrays.asList(
                new News(1, "Title 1", "Content 1", null, null, "Joe Biden"),
                new News(2, "Title 2", "Content 2", null, null, "Hunter Biden")
        );

        when(newsRepository.findAll()).thenReturn(newsList);

        List<News> result = newsService.findAll();

        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void testUpdateNews() {
        Integer newsId = 1;
        News updatedNews = new News(newsId, "Updated News", "Updated content.", null, null, "Joe Biden");

        when(newsRepository.existsById(newsId)).thenReturn(true);
        when(newsRepository.save(updatedNews)).thenReturn(updatedNews);

        News result = newsService.update(newsId, updatedNews);

        assertEquals("Updated News", result.getTitle());
        assertEquals("Updated content.", result.getContent());
        assertEquals("Joe Biden", result.getAuthor());
        verify(newsRepository, times(1)).existsById(newsId);
        verify(newsRepository, times(1)).save(updatedNews);
    }

    @Test
    void testDeleteNews() {
        Integer newsId = 1;

        doNothing().when(newsRepository).deleteById(newsId);

        boolean isDeleted = newsService.delete(newsId);

        assertTrue(isDeleted);
        verify(newsRepository, times(1)).deleteById(newsId);
    }
}
