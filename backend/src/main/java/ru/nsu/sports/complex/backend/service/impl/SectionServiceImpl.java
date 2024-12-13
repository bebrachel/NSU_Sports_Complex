package ru.nsu.sports.complex.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.converter.ScheduleConverter;
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.ScheduleDTO;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Member;
import ru.nsu.sports.complex.backend.model.Schedule;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.TimeSlot;
import ru.nsu.sports.complex.backend.repository.MemberRepository;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Section findById(Integer id) {
        Section section = sectionRepository.findById(id).orElse(null);
        if (section == null) {
            throw new NoSuchElementException("Section with ID " + id + " does not exist");
        }
        return section;
    }

    @Transactional
    @Override
    public Section findByName(String name) {
        Section section = sectionRepository.findByName(name);
        if (section == null) {
            throw new NoSuchElementException("Section with name '" + name + "' does not exist");
        }
        return section;
    }

    @Transactional
    @Override
    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

    @Transactional
    @Override
    public Section createSection(SectionDTO newSectionDTO) {
        if (sectionRepository.findByName(newSectionDTO.getName()) != null) {
            throw new IllegalArgumentException("Section with name '" + newSectionDTO.getName() + "' already exists");
        }
        Section newSection = SectionConverter.dtoToSection(newSectionDTO);
        for (TimeSlot timeSlot : newSection.getSchedule().getTimeSlots()) {
            timeSlot.setSchedule(newSection.getSchedule());
        }
        return sectionRepository.save(newSection);
    }

    @Transactional
    @Override
    public Section updateSection(Integer id, SectionDTO updatedSsectionDTO) {
        Section oldSection = sectionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Section not found with id: " + id));
        if (updatedSsectionDTO.getName() != null) {
            oldSection.setName(updatedSsectionDTO.getName());
        }
        if (updatedSsectionDTO.getPlace() != null) {
            oldSection.setPlace(updatedSsectionDTO.getPlace());
        }
        if (updatedSsectionDTO.getTeacher() != null) {
            oldSection.setTeacher(updatedSsectionDTO.getTeacher());
        }
        ScheduleDTO updatedScheduleDTO = updatedSsectionDTO.getSchedule();
        if (updatedScheduleDTO != null) {
            Schedule updatedSchedule = ScheduleConverter.dtoToSchedule(updatedScheduleDTO);
            Schedule oldSchedule = oldSection.getSchedule();
            oldSchedule.getTimeSlots().clear();
            List<TimeSlot> timeSlots = updatedSchedule.getTimeSlots();
            for (TimeSlot timeSlot : timeSlots) {
                timeSlot.setSchedule(oldSchedule);
                oldSchedule.getTimeSlots().add(timeSlot);
            }
        }
        return sectionRepository.save(oldSection);
    }

    @Transactional
    @Override
    public boolean deleteSectionById(Integer id) {
        Section section = sectionRepository.findById(id).orElse(null);
        if (section == null) {
            return false;
        }
        sectionRepository.delete(section);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteSectionByName(String name) {
        Section section = sectionRepository.findByName(name);
        if (section == null) {
            return false;
        }
        sectionRepository.delete(section);
        return true;
    }

    @Transactional
    public void removeMemberFromSection(Integer memberId, Integer sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new EntityNotFoundException("Секция не найдена"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Мембер не найден"));

        if (!section.getMembers().contains(member)) {
            throw new IllegalStateException("В этой секции нет такого мембера");
        }

        member.leaveSection(section);
        memberRepository.save(member);
    }
}
