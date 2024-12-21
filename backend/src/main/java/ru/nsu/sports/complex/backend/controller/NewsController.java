package ru.nsu.sports.complex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.sports.complex.backend.converter.NewsConverter;
import ru.nsu.sports.complex.backend.dto.NewsDTO;
import ru.nsu.sports.complex.backend.model.News;
import ru.nsu.sports.complex.backend.service.NewsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);
    private final NewsService newsService;
    private final NewsConverter newsConverter;

    @Autowired
    public NewsController(NewsService newsService, NewsConverter newsConverter) {
        this.newsService = newsService;
        this.newsConverter = newsConverter;
    }

    @Operation(summary = "Получить список всех новостей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список новостей.")
    })
    @GetMapping
    public ResponseEntity<List<NewsDTO>> loadAll() {
        LOGGER.info("Fetching all news");
        List<NewsDTO> newsDTOList = newsService.findAll()
                .stream()
                .map(newsConverter::newsToDTO)
                .toList();
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Получить новость по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость."),
            @ApiResponse(responseCode = "404", description = "Новость не найдена.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> loadOne(@PathVariable Integer id) {
        try {
            News news = newsService.find(id);
            return new ResponseEntity<>(newsConverter.newsToDTO(news), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.error("Error fetching news with id {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Создать новую новость.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Новость создана."),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации.")
    })
    @PostMapping
    public ResponseEntity<NewsDTO> create(@RequestBody NewsDTO newsDTO) {
        LOGGER.info("Creating news: {}", newsDTO);
        News news = newsConverter.dtoToNews(newsDTO);
        news = newsService.create(news);
        return new ResponseEntity<>(newsConverter.newsToDTO(news), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить новость по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость обновлена."),
            @ApiResponse(responseCode = "404", description = "Новость не найдена.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> update(@PathVariable Integer id, @RequestBody NewsDTO newsDTO) {
        LOGGER.info("Updating news with id {}: {}", id, newsDTO);
        News updatedNews = newsService.update(id, newsConverter.dtoToNews(newsDTO));
        return new ResponseEntity<>(newsConverter.newsToDTO(updatedNews), HttpStatus.OK);
    }

    @Operation(summary = "Удалить новость по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Новость удалена."),
            @ApiResponse(responseCode = "404", description = "Новость не найдена.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (newsService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
