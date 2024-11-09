package ru.nsu.sports.complex.backend.converter;

import org.springframework.stereotype.Component;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.User;

import java.util.Set;


@Component
public class SectionConverter {
    public Section DTOtoSection(SectionDTO sectionDTO) {
        Section section = new Section();
        section.setName(sectionDTO.getName());
        section.setTeacher(sectionDTO.getTeacher());
        section.setPlace(sectionDTO.getPlace());
        section.setSchedule(sectionDTO.getSchedule());
        section.setCapacity(sectionDTO.getCapacity());
        return section;
    }

    public SectionDTO sectionToDTO(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setName(section.getName());
        sectionDTO.setTeacher(section.getTeacher());
        sectionDTO.setPlace(section.getPlace());
        sectionDTO.setSchedule(section.getSchedule());
        sectionDTO.setCapacity(section.getCapacity());
        sectionDTO.setUsers(section.getUsers());

        return sectionDTO;
    }

}
