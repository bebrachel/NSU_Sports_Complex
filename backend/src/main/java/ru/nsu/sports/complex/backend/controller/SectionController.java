package ru.nsu.sports.complex.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@AllArgsConstructor
public class SectionController {
    private final SectionService service;

    @Operation(summary = "Получить информацию о секции по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Section.class))
            })})
    @GetMapping("/{id}")
    public ResponseEntity<Section> findById(@PathVariable Integer id) {
        Section section = service.findById(id);
        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Получить информацию о секции по имени.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Section.class))
            })})
    @GetMapping("/name/{name}")
    public ResponseEntity<Section> findByName(@PathVariable String name) {
        Section section = service.findByName(name);
        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Получить список созданных секций.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список секций.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = List.class))
            })})
    @GetMapping
    public List<Section> findAllSections() {
        return service.findAllSections();
    }

    @Operation(summary = "Создать новую секцию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Созданная секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Section.class))
            })})
    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        try {
            Section newSection = service.createSection(section);
            return new ResponseEntity<>(newSection, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Удалить секцию по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " true -- секция с таким id существует и удаление успешно /n false -- иначе", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Boolean.class))
            })})
    @DeleteMapping("/{id}")
    public boolean deleteSection(@PathVariable Integer id) {
        return service.deleteSection(id);
    }
}
