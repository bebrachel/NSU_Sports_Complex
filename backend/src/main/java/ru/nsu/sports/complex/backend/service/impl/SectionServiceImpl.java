package ru.nsu.sports.complex.backend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;

@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository repository;

    @Override
    public Section findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // ПОТЕТСТИТЬ, что будет, если такого не будет
    @Override
    public Section findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Section> findAllSections() {
        return repository.findAll();
    }

    @Override
    public Section saveSection(Section section) {
        return repository.save(section);
    }

    // ПОТЕТСТИТЬ, точно ли он заменит, а не создаст дубликат
    // Не заменяет, надо думать
    @Override
    public Section updateSection(Section section) {
        return repository.save(section);
    }

    @Override
    public boolean deleteByName(String name) {
        return repository.deleteByName(name);
    }
}
