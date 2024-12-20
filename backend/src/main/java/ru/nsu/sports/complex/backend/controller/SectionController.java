package ru.nsu.sports.complex.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/sections")
@AllArgsConstructor
public class SectionController {
    private final SectionService service;

    @Operation(summary = "Получить информацию о секции по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionDTO.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> findById(@PathVariable Integer id) {
        Section section = service.findById(id);
        return new ResponseEntity<>(SectionConverter.sectionToDTO(section), HttpStatus.OK);
    }

    @Operation(summary = "Получить информацию о секции по названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Section.class))
            })
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Section> findByName(@PathVariable String name) {
        Section section = service.findByName(name);
        return new ResponseEntity<>(section, HttpStatus.OK);
    }

    @Operation(summary = "Получить список созданных секций.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список секций.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = List.class))
            })
    })
    @GetMapping
    public List<SectionDTO> findAllSections() {
        return service.findAllSections().stream()
                .map(SectionConverter::sectionToDTO)
                .toList();
    }

    @Operation(summary = "Создать новую секцию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Созданная секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionDTO.class))
            })
    })
    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO newSectionDTO) {
        Section createdSection = service.createSection(newSectionDTO);
        return new ResponseEntity<>(SectionConverter.sectionToDTO(createdSection), HttpStatus.OK);
    }

    @Operation(summary = "Обновить поля секции.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленная секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionDTO.class))
            })
    })
    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(@PathVariable Integer id, @RequestBody SectionDTO updatedSectionDTO) {
        Section updatedSection = service.updateSection(id, updatedSectionDTO);
        return new ResponseEntity<>(SectionConverter.sectionToDTO(updatedSection), HttpStatus.OK);
    }

    @Operation(summary = "Удалить секцию по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))
            })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSectionById(@PathVariable Integer id) {
        boolean isDeleted = service.deleteSectionById(id);
        if (isDeleted) {
            return new ResponseEntity<>("Section deleted successfully", HttpStatus.OK);
        } else {
            throw new NoSuchElementException("Section with id '" + id + "' does not exist");
        }
    }

    @Operation(summary = "Удалить секцию по названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))
            })
    })
    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteSectionByName(@PathVariable String name) {
        boolean isDeleted = service.deleteSectionByName(name);
        if (isDeleted) {
            return new ResponseEntity<>("Section deleted successfully", HttpStatus.OK);
        } else {
            throw new NoSuchElementException("Section with name '" + name + "' does not exist");
        }
    }
}