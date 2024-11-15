package ru.nsu.sports.complex.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.impl.SectionServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SectionServiceImplTest {

    @Mock
    private SectionRepository repository;

    @InjectMocks
    private SectionServiceImpl service;

    private Section section1;
    private Section newSection;

    @BeforeEach
    void setup() {
        // Создание тестовых данных
        section1 = new Section("Плавание");
        section1.setId(1);
        section1.setPlace("Бассейн НГУ");
        section1.setSchedule("расписание");
        section1.setTeacher("Тимофеев С. И.");

        newSection = new Section("New section");
        newSection.setPlace("New place");
        newSection.setTeacher("New teacher");
        newSection.setSchedule("New schedule");
        newSection.setId(2);
    }

    @Test
    void testFindById_Success() {
        when(repository.findById(section1.getId())).thenReturn(Optional.of(section1));

        Section result = service.findById(section1.getId());
        assertEquals(result, section1);
        verify(repository, times(1)).findById(section1.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(section1.getId())).thenReturn(Optional.empty());

        Section result = service.findById(section1.getId());

        assertNull(result);
        verify(repository, times(1)).findById(section1.getId());
    }

    @Test
    void testFindByName_Success() {
        when(repository.findByName(section1.getName())).thenReturn(section1);

        Section result = service.findByName(section1.getName());
        assertEquals(result, section1);
        verify(repository, times(1)).findByName(section1.getName());
    }

    @Test
    void testFindByName_NotFound() {
        when(repository.findByName(section1.getName())).thenReturn(null);

        Section result = service.findByName(section1.getName());

        assertNull(result);
        verify(repository, times(1)).findByName(section1.getName());
    }

    @Test
    void testFindAllSections() {
        when(repository.findAll()).thenReturn(List.of(section1));

        assertEquals(service.findAllSections().size(), 1);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testCreateSection() {
        when(repository.save(newSection)).thenReturn(newSection);

        Section section = service.createSection(newSection);
        assertEquals(section, newSection);
        verify(repository, times(1)).save(newSection);
    }

    @Test
    void testUpdateSection_Success() {
        when(repository.findById(section1.getId())).thenReturn(Optional.of(section1));
        when(repository.save(section1)).thenReturn(section1);

        Section updatedSection = service.updateSection(newSection, section1.getId());

        assertNotNull(updatedSection);
        assertEquals(updatedSection.getName(), newSection.getName());
        assertEquals(updatedSection.getTeacher(), newSection.getTeacher());
        assertEquals(updatedSection.getSchedule(), newSection.getSchedule());
        assertEquals(updatedSection.getPlace(), newSection.getPlace());

        verify(repository, times(1)).findById(section1.getId());
        verify(repository, times(1)).save(section1);
    }

    @Test
    void testUpdateSection_NotFound() {
        when(repository.findById(section1.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.updateSection(newSection, section1.getId()));

        verify(repository, times(1)).findById(section1.getId());
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteSectionById_Success() {
        when(repository.findById(section1.getId())).thenReturn(Optional.of(section1));

        boolean result = service.deleteSectionById(section1.getId());

        assertTrue(result);
        verify(repository, times(1)).findById(section1.getId());
        verify(repository, times(1)).delete(section1);
    }

    @Test
    void testDeleteSectionById_NotFound() {
        when(repository.findById(section1.getId())).thenReturn(Optional.empty());

        boolean result = service.deleteSectionById(section1.getId());

        assertFalse(result);
        verify(repository, times(1)).findById(section1.getId());
        verify(repository, never()).delete(any(Section.class));
    }

    @Test
    void testDeleteSectionByName_Success() {
        when(repository.findByName(section1.getName())).thenReturn(section1);

        boolean result = service.deleteSectionByName(section1.getName());

        assertTrue(result);
        verify(repository, times(1)).findByName(section1.getName());
        verify(repository, times(1)).delete(section1);
    }

    @Test
    void testDeleteSectionByName_NotFound() {
        when(repository.findByName(section1.getName())).thenReturn(null);

        boolean result = service.deleteSectionByName(section1.getName());

        assertFalse(result);
        verify(repository, times(1)).findByName(section1.getName());
        verify(repository, never()).delete(any(Section.class));
    }
}
