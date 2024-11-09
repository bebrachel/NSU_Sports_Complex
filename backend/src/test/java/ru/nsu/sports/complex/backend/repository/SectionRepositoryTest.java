package ru.nsu.sports.complex.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nsu.sports.complex.backend.model.Section;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class SectionRepositoryTest {


    @Autowired
    private SectionRepository repository;

    // Это заодно и тестирование метода save()
    @BeforeEach
    public void setup() {
        // Очистка таблицы перед каждым тестом
        repository.deleteAll();

        // Создание тестовых данных
        Section section1 = new Section();
        section1.setName("Плавание");
        section1.setPlace("Бассейн НГУ");
        section1.setSchedule("расписание");
        section1.setTeacher("Тимофеев С. И.");
        repository.save(section1);

        Section section2 = new Section();
        section2.setName("Настольный теннис");
        section2.setPlace("СКЦ (цокольный этаж, Пирогова, 12/1)");
        section2.setSchedule("расписание");
        section2.setTeacher("Троценко Д.А.");
        repository.save(section2);
    }

    @Test
    public void testFindByName() {
        Section section = repository.findByName("Плавание");
        assertNotNull(section);
        assertEquals("Бассейн НГУ", section.getPlace());
        assertEquals("Тимофеев С. И.", section.getTeacher());
    }

    @Test
    public void testFindById() {
        Section sectionByName = repository.findByName("Плавание");
        assertNotNull(sectionByName);
        Integer id = sectionByName.getId();
        Optional<Section> sectionById = repository.findById(id);
        assert (sectionById.isPresent());
        assertEquals(sectionByName.getName(), sectionById.get().getName());
        assertEquals(sectionByName.getId(), sectionById.get().getId());
        assertEquals(sectionByName.getPlace(), sectionById.get().getPlace());
        assertEquals(sectionByName.getSchedule(), sectionById.get().getSchedule());
    }

    @Test
    public void testFindAll() {
        List<Section> sections = repository.findAll();
        assertEquals(sections.size(), 2);
    }

    @Test
    public void testDelete() {
        List<Section> sections = repository.findAll();
        int count = sections.size();
        Section section = sections.get(0);
        repository.delete(section);
        assertNull(repository.findByName(section.getName()));
        assertEquals(repository.findAll().size(), count - 1);
    }

    @Test
    public void testDeleteAll() {
        int count = repository.findAll().size();
        assertNotEquals(count, 0);
        repository.deleteAll();
        assertEquals(repository.findAll().size(), 0);
    }
}
