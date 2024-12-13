package ru.nsu.sports.complex.backend.converter;

import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.model.Section;

import java.util.stream.Collectors;

public class SectionConverter {
    private SectionConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Section dtoToSection(SectionDTO sectionDTO) {
        if (sectionDTO.getName() == null || sectionDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }

        Section section = new Section();

        section.setId(sectionDTO.getId());
        section.setName(sectionDTO.getName());
        section.setTeacher(sectionDTO.getTeacher());
        section.setPlace(sectionDTO.getPlace());
        section.setCapacity(sectionDTO.getCapacity());

        section.setSchedule(ScheduleConverter.dtoToSchedule(sectionDTO.getSchedule()));

        if (sectionDTO.getMembers() != null) {
            section.setMembers(sectionDTO.getMembers().stream()
                    .map(memberDTO -> {
                        Member member = new Member();
                        member.setId(memberDTO.getId());
                        return member;
                    })
                    .collect(Collectors.toSet()));
        }

        return section;
    }

    public static SectionDTO sectionToDTO(Section section) {

        SectionDTO sectionDTO = new SectionDTO();

        sectionDTO.setId(section.getId());
        sectionDTO.setName(section.getName());
        sectionDTO.setTeacher(section.getTeacher());
        sectionDTO.setPlace(section.getPlace());
        sectionDTO.setCapacity(section.getCapacity());

        sectionDTO.setSchedule(ScheduleConverter.scheduleToDTO(section.getSchedule()));

        if (section.getMembers() != null) {
            sectionDTO.setMembers(section.getMembers().stream()
                    .map(member -> {
                        Member memberDTO = new Member();
                        memberDTO.setId(member.getId());
                        memberDTO.setName(member.getName());
                        return memberDTO;
                    })
                    .collect(Collectors.toSet()));
        }

        return sectionDTO;
    }
}
