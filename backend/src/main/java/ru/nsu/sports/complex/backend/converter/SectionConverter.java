package ru.nsu.sports.complex.backend.converter;

import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;

public class SectionConverter {
    private SectionConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Section dtoToSection(SectionDTO sectionDTO) {
        if (sectionDTO.getName() == null || sectionDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        Section section = new Section();
        section.setName(sectionDTO.getName());
        section.setTeacher(sectionDTO.getTeacher());
        section.setPlace(sectionDTO.getPlace());
        section.setSchedule(ScheduleConverter.dtoToSchedule(sectionDTO.getSchedule()));
        return section;
    }

    public static SectionDTO sectionToDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName(section.getName());
        sectionDTO.setTeacher(section.getTeacher());
        sectionDTO.setPlace(section.getPlace());
        sectionDTO.setSchedule(ScheduleConverter.scheduleToDTO(section.getSchedule()));
        return sectionDTO;
    }
}
