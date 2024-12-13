package ru.nsu.sports.complex.backend.service;

import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;

import java.util.List;


public interface SectionService {
    Section findById(Integer id);
    Section findByName(String name);
    List<Section> findAllSections();
    Section createSection(SectionDTO newSectionDTO);
    Section updateSection(Integer id, SectionDTO updatedSsectionDTO);
    boolean deleteSectionById(Integer id);
    boolean deleteSectionByName(String name);
    void removeMemberFromSection();
}
