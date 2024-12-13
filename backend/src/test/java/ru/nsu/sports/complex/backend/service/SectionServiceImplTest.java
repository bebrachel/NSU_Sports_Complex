package ru.nsu.sports.complex.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Schedule;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.TimeSlot;
import ru.nsu.sports.complex.backend.repository.SectionRepository;
import ru.nsu.sports.complex.backend.service.impl.SectionServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
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
        Schedule schedule1 = new Schedule();
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        timeSlots.add(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        schedule1.setTimeSlots(timeSlots);
        section1.setSchedule(schedule1);
        section1.setTeacher("Тимофеев С. И.");

        newSection = new Section("Настольный теннис");
        newSection.setId(2);
        newSection.setPlace("СКЦ (цокольный этаж, Пирогова, 12/1)");
        Schedule schedule2 = new Schedule();
        List<TimeSlot> timeSlots2 = new ArrayList<>();
        timeSlots2.add(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        timeSlots2.add(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        schedule2.setTimeSlots(timeSlots2);
        newSection.setSchedule(schedule2);
        newSection.setTeacher("Троценко Д.А.");
    }

    private void equalsSections(Section section1, Section section2) {
        assertEquals(section1.getId(), section2.getId());
        assertEquals(section1.getName(), section2.getName());
        assertEquals(section1.getTeacher(), section2.getTeacher());
        assertEquals(section1.getPlace(), section2.getPlace());

        assertEquals(section1.getSchedule().getId(), section2.getSchedule().getId());
        var timeSlots1 = section1.getSchedule().getTimeSlots();
        var timeSlots2 = section2.getSchedule().getTimeSlots();
        for (int i = 0; i < timeSlots1.size(); i++) {
            var ts1 = timeSlots1.get(i);
            var ts2 = timeSlots2.get(i);
            assertEquals(ts1.getId(), ts2.getId());
            assertEquals(ts1.getStartTime(), ts2.getStartTime());
            assertEquals(ts1.getEndTime(), ts2.getEndTime());
            assertEquals(ts1.getDayOfWeek(), ts2.getDayOfWeek());
        }
    }

    @Test
    void testFindById_Success() {
        Integer id = section1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(section1));

        Section result = service.findById(id);
        equalsSections(result, section1);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        Integer id = section1.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findById(id));

        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByName_Success() {
        String name = section1.getName();
        when(repository.findByName(name)).thenReturn(section1);

        Section result = service.findByName(name);
        equalsSections(result, section1);
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testFindByName_NotFound() {
        String name = section1.getName();
        when(repository.findByName(name)).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findByName(name));

        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testFindAllSections() {
        when(repository.findAll()).thenReturn(List.of(section1));

        assertEquals(1, service.findAllSections().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testCreateSection() {
        when(repository.save(any())).thenReturn(newSection);

        Section result = service.createSection(SectionConverter.sectionToDTO(newSection));
        equalsSections(result, newSection);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdateSection_Success() {
        Integer id = section1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(section1));
        when(repository.save(any())).thenReturn(section1);

        SectionDTO newSectionDTO = SectionConverter.sectionToDTO(newSection);
        Section updatedSection = service.updateSection(id, newSectionDTO);

        assertNotNull(updatedSection);
        assertEquals(updatedSection.getName(), newSection.getName());
        assertEquals(updatedSection.getTeacher(), newSection.getTeacher());
        assertEquals(updatedSection.getSchedule().getTimeSlots().size(), newSection.getSchedule().getTimeSlots().size());
        assertEquals(updatedSection.getPlace(), newSection.getPlace());

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdateSection_NotFound() {
        Integer id = section1.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        SectionDTO newSectionDTO = SectionConverter.sectionToDTO(newSection);
        assertThrows(NoSuchElementException.class, () -> service.updateSection(id, newSectionDTO));

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteSectionById_Success() {
        Integer id = section1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(section1));

        assertTrue(service.deleteSectionById(id));

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).delete(section1);
    }

    @Test
    void testDeleteSectionById_NotFound() {
        Integer id = section1.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertFalse(service.deleteSectionById(id));

        verify(repository, times(1)).findById(id);
        verify(repository, never()).delete(any(Section.class));
    }

    @Test
    void testDeleteSectionByName_Success() {
        String name = section1.getName();
        when(repository.findByName(name)).thenReturn(section1);

        assertTrue(service.deleteSectionByName(name));

        verify(repository, times(1)).findByName(name);
        verify(repository, times(1)).delete(section1);
    }

    @Test
    void testDeleteSectionByName_NotFound() {
        String name = section1.getName();
        when(repository.findByName(name)).thenReturn(null);

        assertFalse(service.deleteSectionByName(name));

        verify(repository, times(1)).findByName(name);
        verify(repository, never()).delete(any(Section.class));
    }
}
