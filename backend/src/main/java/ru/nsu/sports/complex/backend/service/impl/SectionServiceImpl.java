package ru.nsu.sports.complex.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.TimeSlot;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;

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
    public Section createSection(SectionDTO sectionDTO) {
        Section section = SectionConverter.dtoToSection(sectionDTO);
        for (TimeSlot timeSlot : section.getSchedule().getTimeSlots()) {
            timeSlot.setSchedule(section.getSchedule());
        }
        return sectionRepository.save(section);
    }

    @Transactional
    @Override
    public Section updateSection(SectionDTO sectionDTO, Integer id) {
        Section section = SectionConverter.dtoToSection(sectionDTO);
        Section sectionInDB = sectionRepository.findById(id).orElseThrow();
        if (section.getName() != null) {
            sectionInDB.setName(section.getName());
        }
        if (section.getPlace() != null) {
            sectionInDB.setPlace(section.getPlace());
        }
        if (section.getTeacher() != null) {
            sectionInDB.setTeacher(section.getTeacher());
        }
        if (section.getSchedule() != null) {
            sectionInDB.setSchedule(section.getSchedule());
        }
        return sectionRepository.save(sectionInDB);
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
}
