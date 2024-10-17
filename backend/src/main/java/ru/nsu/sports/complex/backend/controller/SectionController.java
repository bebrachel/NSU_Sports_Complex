package ru.nsu.sports.complex.backend.controller;


import lombok.AllArgsConstructor;
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

    @GetMapping("/id/{id}")
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
    public Section saveSection(@RequestBody Section section) {
        return service.saveSection(section);
    }

    @PutMapping
    public Section updateSection(@RequestBody Section section) {
        return service.updateSection(section);
    }

    @DeleteMapping("/delete/{name}")
    public boolean deleteSection(@PathVariable String name) {
        return service.deleteByName(name);
    }
}
