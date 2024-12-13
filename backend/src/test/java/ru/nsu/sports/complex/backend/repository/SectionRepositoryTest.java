package ru.nsu.sports.complex.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nsu.sports.complex.backend.model.Schedule;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.TimeSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SectionRepositoryTest {


    @Autowired
    private SectionRepository repository;

    private Section section1;

    // Это заодно и тестирование метода save()
    @BeforeEach
    void setup() {
        // Очистка таблицы перед каждым тестом
        repository.deleteAll();

        // Создание тестовых данных
        section1 = new Section("Плавание");
        section1.setPlace("Бассейн НГУ");
        Schedule schedule1 = new Schedule();
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        timeSlots.add(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        schedule1.setTimeSlots(timeSlots);
        section1.setSchedule(schedule1);
        section1.setTeacher("Тимофеев С. И.");
        repository.save(section1);

        Section section2 = new Section("Настольный теннис");
        section2.setPlace("СКЦ (цокольный этаж, Пирогова, 12/1)");
        Schedule schedule2 = new Schedule();
        List<TimeSlot> timeSlots2 = new ArrayList<>();
        timeSlots2.add(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        timeSlots2.add(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        schedule2.setTimeSlots(timeSlots2);
        section2.setSchedule(schedule2);
        section2.setTeacher("Троценко Д.А.");
        repository.save(section2);
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
    void testFindByName() {
        Section section = repository.findByName(section1.getName());
        equalsSections(section, section1);
    }

    @Test
    void testFindById() {
        Section sectionByName = repository.findByName(section1.getName());
        assertNotNull(sectionByName);
        Integer id = sectionByName.getId();
        Optional<Section> sectionById = repository.findById(id);
        assert (sectionById.isPresent());
        equalsSections(sectionByName, sectionById.get());
    }

    @Test
    void testFindAll() {
        List<Section> sections = repository.findAll();
        assertEquals(2, sections.size());
    }

    @Test
    void testDelete() {
        List<Section> sections = repository.findAll();
        int count = sections.size();
        Section section = sections.get(0);
        repository.delete(section);
        assertNull(repository.findByName(section.getName()));
        assertEquals(count - 1, repository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        int count = repository.findAll().size();
        assertNotEquals(0, count);
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
    }
}
