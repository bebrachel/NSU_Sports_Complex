package ru.nsu.sports.complex.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nsu.sports.complex.backend.converter.NewsConverter;
import ru.nsu.sports.complex.backend.dto.NewsDTO;
import ru.nsu.sports.complex.backend.model.News;
import ru.nsu.sports.complex.backend.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NewsControllerTest {

    @Mock
    private NewsService newsService;

    @Mock
    private NewsConverter newsConverter;

    @InjectMocks
    private NewsController newsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadAllNews() {
        List<News> newsList = List.of(
                new News(1, "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), "Author 1"),
                new News(2, "Title 2", "Content 2", LocalDateTime.now(), LocalDateTime.now(), "Author 2")
        );

        List<NewsDTO> newsDTOList = List.of(
                new NewsDTO(1, "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), "Author 1"),
                new NewsDTO(2, "Title 2", "Content 2", LocalDateTime.now(), LocalDateTime.now(), "Author 2")
        );

        when(newsService.findAll()).thenReturn(newsList);
        when(newsConverter.newsToDTO(newsList.get(0))).thenReturn(newsDTOList.get(0));
        when(newsConverter.newsToDTO(newsList.get(1))).thenReturn(newsDTOList.get(1));

        ResponseEntity<List<NewsDTO>> response = newsController.loadAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(newsService, times(1)).findAll();
    }

    @Test
    void testLoadOneNews() {
        News news = new News(1, "Title", "Content", LocalDateTime.now(), LocalDateTime.now(), "Author");
        NewsDTO newsDTO = new NewsDTO(1, "Title", "Content", LocalDateTime.now(), LocalDateTime.now(), "Author");

        when(newsService.find(1)).thenReturn(news);
        when(newsConverter.newsToDTO(news)).thenReturn(newsDTO);

        ResponseEntity<NewsDTO> response = newsController.loadOne(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newsDTO, response.getBody());
    }

    @Test
    void testCreateNews() {
        NewsDTO newsDTO = new NewsDTO(null, "Title", "Content", LocalDateTime.now(), LocalDateTime.now(), "Author");
        News news = new News(null, "Title", "Content", LocalDateTime.now(), LocalDateTime.now(), "Author");
        News savedNews = new News(1, "Title", "Content", LocalDateTime.now(), LocalDateTime.now(), "Author");

        when(newsConverter.dtoToNews(newsDTO)).thenReturn(news);
        when(newsService.create(news)).thenReturn(savedNews);
        when(newsConverter.newsToDTO(savedNews)).thenReturn(new NewsDTO(1, "Title", "Content", LocalDateTime.now(), LocalDateTime.now(), "Author"));

        ResponseEntity<NewsDTO> response = newsController.create(newsDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testUpdateNews() {
        Integer newsId = 1;
        NewsDTO newsDTO = new NewsDTO(newsId, "Updated Title", "Updated Content", LocalDateTime.now(), LocalDateTime.now(), "Updated Author");
        News updatedNews = new News(newsId, "Updated Title", "Updated Content", LocalDateTime.now(), LocalDateTime.now(), "Updated Author");

        when(newsConverter.dtoToNews(newsDTO)).thenReturn(updatedNews);
        when(newsService.update(newsId, updatedNews)).thenReturn(updatedNews);
        when(newsConverter.newsToDTO(updatedNews)).thenReturn(newsDTO);

        ResponseEntity<NewsDTO> response = newsController.update(newsId, newsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", response.getBody().getTitle());
    }

    @Test
    void testDeleteNews() {
        when(newsService.delete(1)).thenReturn(true);

        ResponseEntity<Void> response = newsController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(newsService, times(1)).delete(1);
    }
}
