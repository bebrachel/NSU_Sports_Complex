package ru.nsu.sports.complex.backend.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.sports.complex.backend.dto.NewsDTO;
import ru.nsu.sports.complex.backend.model.News;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NewsConverterTest {

    private NewsConverter newsConverter;

    @BeforeEach
    void setUp() {
        newsConverter = new NewsConverter();
    }

    @Test
    void testDtoToNews() {
        NewsDTO newsDTO = new NewsDTO(
                1,
                "Title 1",
                "Content 1",
                LocalDateTime.of(2024, 12, 6, 10, 0, 0),
                LocalDateTime.of(2024, 12, 6, 10, 0, 0),
                "Joe Biden"
        );

        News news = newsConverter.dtoToNews(newsDTO);

        assertNotNull(news);
        assertEquals(newsDTO.getId(), news.getId());
        assertEquals(newsDTO.getTitle(), news.getTitle());
        assertEquals(newsDTO.getContent(), news.getContent());
        assertEquals(newsDTO.getCreatedAt(), news.getCreatedAt());
        assertEquals(newsDTO.getUpdatedAt(), news.getUpdatedAt());
        assertEquals(newsDTO.getAuthor(), news.getAuthor());
    }

    @Test
    void testNewsToDTO() {
        News news = new News(
                1,
                "Title 1",
                "Content 1",
                LocalDateTime.of(2024, 12, 6, 10, 0, 0),
                LocalDateTime.of(2024, 12, 6, 10, 0, 0),
                "Joe Biden"
        );

        NewsDTO newsDTO = newsConverter.newsToDTO(news);

        assertNotNull(newsDTO);
        assertEquals(news.getId(), newsDTO.getId());
        assertEquals(news.getTitle(), newsDTO.getTitle());
        assertEquals(news.getContent(), newsDTO.getContent());
        assertEquals(news.getCreatedAt(), newsDTO.getCreatedAt());
        assertEquals(news.getUpdatedAt(), newsDTO.getUpdatedAt());
        assertEquals(news.getAuthor(), newsDTO.getAuthor());
    }
}
