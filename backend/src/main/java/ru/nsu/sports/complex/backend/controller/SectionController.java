package ru.nsu.sports.complex.backend.controller;


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

    @GetMapping("/{id}")
    public ResponseEntity<Section> findById(@PathVariable Integer id) {
        Section section = service.findById(id);
        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Section> findByName(@PathVariable String name) {
        Section section = service.findByName(name);
        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Section> findAllSections() {
        return service.findAllSections();
    }

    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        try {
            Section newSection = service.createSection(section);
            return new ResponseEntity<>(newSection, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public boolean deleteSection(@PathVariable Integer id) {
        return service.deleteSection(id);
    }
}
