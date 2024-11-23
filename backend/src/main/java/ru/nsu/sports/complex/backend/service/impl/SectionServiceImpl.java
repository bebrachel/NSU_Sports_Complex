package ru.nsu.sports.complex.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository repository;

    @Transactional
    @Override
    public Section findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Section findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    @Override
    public List<Section> findAllSections() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public Section createSection(Section section) {
        if (repository.findByName(section.getName()) != null) {
            throw new IllegalArgumentException("Section with name '" + section.getName() + "' already exists");
        }
        return repository.save(section);
    }

    @Transactional
    @Override
    public Section updateSection(Section section, Integer id) {
        Section sectionInDB = repository.findById(id).orElseThrow();
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
        return repository.save(sectionInDB);
    }

    @Transactional
    @Override
    public boolean deleteSectionById(Integer id) {
        Section section = repository.findById(id).orElse(null);
        System.out.println(section);
        if (section == null) {
            return false;
        }
        try {
            repository.delete(section);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete section due to related data", e);
        }
    }


    @Transactional
    @Override
    public boolean deleteSectionByName(String name) {
        Section section = repository.findByName(name);
        System.out.println(section);
        if (section == null) {
            return false;
        }
        try {
            repository.delete(section);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete section due to related data", e);
        }
    }
}
