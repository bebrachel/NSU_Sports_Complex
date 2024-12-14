package ru.nsu.sports.complex.backend.converter;

import org.springframework.stereotype.Component;
import ru.nsu.sports.complex.backend.dto.NewsDTO;
import ru.nsu.sports.complex.backend.model.News;

@Component
public class NewsConverter {

    public News dtoToNews(NewsDTO newsDTO) {
        News news = new News();
        news.setId(newsDTO.getId());
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setCreatedAt(newsDTO.getCreatedAt());
        news.setUpdatedAt(newsDTO.getUpdatedAt());
        news.setAuthor(newsDTO.getAuthor());
        return news;
    }

    public NewsDTO newsToDTO(News news) {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(news.getId());
        newsDTO.setTitle(news.getTitle());
        newsDTO.setContent(news.getContent());
        newsDTO.setCreatedAt(news.getCreatedAt());
        newsDTO.setUpdatedAt(news.getUpdatedAt());
        newsDTO.setAuthor(news.getAuthor());
        return newsDTO;
    }
}
