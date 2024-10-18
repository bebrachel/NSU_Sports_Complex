package ru.nsu.sports.complex.backend.service;

import ru.nsu.sports.complex.backend.model.Section;

import java.util.List;


public interface SectionService {
    Section findById(Integer id);
    Section findByName(String name);
    List<Section> findAllSections();
    Section saveSection(Section section);
    //Section updateSection(Section section);
    boolean deleteSection(Integer id);
}
