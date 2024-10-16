package ru.nsu.sports.complex.backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.sports.complex.backend.model.Section;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @GetMapping
    public List<Section> getAllSections() {
        return new ArrayList<>();
    }
}
