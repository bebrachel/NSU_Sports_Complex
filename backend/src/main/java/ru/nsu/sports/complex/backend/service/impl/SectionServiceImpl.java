package ru.nsu.sports.complex.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.converter.ScheduleConverter;
import ru.nsu.sports.complex.backend.dto.ScheduleDTO;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Schedule;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.TimeSlot;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;

    @Transactional
    @Override
    public Section findById(Integer id) {
        return sectionRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Section findByName(String name) {
        return sectionRepository.findByName(name);
    }

    @Transactional
    @Override
    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

    @Transactional
    @Override
    public Section createSection(Section section) {
        for (TimeSlot timeSlot : section.getSchedule().getTimeSlots()) {
            timeSlot.setSchedule(section.getSchedule());
        }
        return sectionRepository.save(section);
    }

    @Transactional
    @Override
    public Section updateSection(Integer id, SectionDTO updatedSectionDTO) {
        Section oldSection = sectionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Section not found with id: " + id));
        if (updatedSectionDTO.getName() != null) {
            oldSection.setName(updatedSectionDTO.getName());
        }
        if (updatedSectionDTO.getPlace() != null) {
            oldSection.setPlace(updatedSectionDTO.getPlace());
        }
        if (updatedSectionDTO.getTeacher() != null) {
            oldSection.setTeacher(updatedSectionDTO.getTeacher());
        }
        ScheduleDTO updatedScheduleDTO = updatedSectionDTO.getSchedule();
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
        try {
            sectionRepository.delete(section);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete section due to related data", e);
        }
    }

    @Transactional
    @Override
    public boolean deleteSectionByName(String name) {
        Section section = sectionRepository.findByName(name);
        if (section == null) {
            return false;
        }
        try {
            sectionRepository.delete(section);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete section due to related data", e);
        }
    }
}
