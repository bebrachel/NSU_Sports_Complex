package ru.nsu.sports.complex.backend.converter;

import org.springframework.stereotype.Component;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;

@Component
public class SectionConverter {
    public Section DTOtoSection(SectionDTO sectionDTO) {
        if (sectionDTO.getName() == null || sectionDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        Section section = new Section(sectionDTO.getName());
        section.setTeacher(sectionDTO.getTeacher());
        section.setPlace(sectionDTO.getPlace());
        section.setSchedule(sectionDTO.getSchedule());
        return section;
    }

    public SectionDTO sectionToDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName(section.getName());
        sectionDTO.setTeacher(section.getTeacher());
        sectionDTO.setPlace(section.getPlace());
        sectionDTO.setSchedule(section.getSchedule());
        return sectionDTO;
    }
}
