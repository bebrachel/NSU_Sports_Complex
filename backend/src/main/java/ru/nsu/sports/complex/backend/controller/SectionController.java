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
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sections")
@AllArgsConstructor
public class SectionController {
    private final SectionService service;
    private final SectionConverter converter;

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
        if (section != null) {
            return new ResponseEntity<>(converter.sectionToDTO(section), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
            })
    })
    @GetMapping
    public List<SectionDTO> findAllSections() {
        return service.findAllSections().stream()
                .map(converter::sectionToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Создать новую секцию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Созданная секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SectionDTO.class))
            })
    })
    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        try {
            Section newSection = service.createSection(converter.DTOtoSection(sectionDTO));
            SectionDTO resultSectionDTO = converter.sectionToDTO(newSection);
            return new ResponseEntity<>(resultSectionDTO, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Обновить поля секции.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленная секция.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Section.class))
            })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable Integer id, @RequestBody Section section) {
        try {
            Section updatedSection = service.updateSection(section, id);
            return new ResponseEntity<>(updatedSection, HttpStatus.OK);
        } catch (NoSuchElementException | DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Удалить секцию по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema())
            })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSectionById(@PathVariable Integer id) {
        if (service.deleteSectionById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Удалить секцию по названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema())
            })
    })
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteSectionByName(@PathVariable String name) {
        if (service.deleteSectionByName(name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
