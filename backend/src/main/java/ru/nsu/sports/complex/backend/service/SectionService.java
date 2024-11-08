package ru.nsu.sports.complex.backend.service;

import ru.nsu.sports.complex.backend.model.Section;

import java.util.List;


public interface SectionService {
    Section findById(Integer id);
    Section findByName(String name);
    List<Section> findAllSections();
    Section createSection(Section section);
    Section updateSection(Section section, Integer id);
    boolean deleteSectionById(Integer id);
    boolean deleteSectionByName(String name);
}
