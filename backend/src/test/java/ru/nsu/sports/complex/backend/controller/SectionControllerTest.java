package ru.nsu.sports.complex.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nsu.sports.complex.backend.configuration.security.JwtAuthenticationFilter;
import ru.nsu.sports.complex.backend.configuration.security.JwtServiceImpl;
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Schedule;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.model.TimeSlot;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SectionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private SectionService service;

    // Метод необходим для создания секции с заданными параметрами.
    // У всех секций создается одинаковое расписание.
    private Section createSection(String name, Integer id, String place, String teacher) {
        Section section = new Section(name);
        section.setId(id);
        section.setPlace(place);
        Schedule schedule = new Schedule();
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        timeSlots.add(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        schedule.setTimeSlots(timeSlots);
        section.setSchedule(schedule);
        section.setTeacher(teacher);
        return section;
    }

    // Use-case: Пользователь хочет найти существующую секцию по некоторому id.
    @Test
    void testFindById_Success() throws Exception {
        // Given: У нас есть желаемая секция, её дто, известный id.
        // Given: Замокано поведение сервиса, возвращает нужную секцию по нашему id.
        Section section = createSection("Плавание", 1, "Бассейн НГУ", "Тимофеев С. И.");
        SectionDTO sectionDTO = SectionConverter.sectionToDTO(section);
        int id = section.getId();
        when(service.findById(id)).thenReturn(section);

        // When: Хотим получить секцию с заданным id, выполняем GET-запрос.
        mockMvc.perform(get("/api/sections/" + id))

                // Then: Проверяем, что секция действительно та, которую и искали.
                .andExpect(status().isOk())
                // Then: Проверяем название, учителя, место.
                .andExpect(jsonPath("$.name").value(sectionDTO.getName()))
                .andExpect(jsonPath("$.teacher").value(sectionDTO.getTeacher()))
                .andExpect(jsonPath("$.place").value(sectionDTO.getPlace()))
                // Then: Проверяем общее расписание.
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(2)))
                // Then: Проверяем первый слот времени.
                .andExpect(jsonPath("$.schedule.timeSlots[0].dayOfWeek").value("FRIDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[0].startTime").value("18:00"))
                .andExpect(jsonPath("$.schedule.timeSlots[0].endTime").value("19:00"))
                // Then: Проверяем второй слот времени.
                .andExpect(jsonPath("$.schedule.timeSlots[1].dayOfWeek").value("WEDNESDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[1].startTime").value("18:00"))
                .andExpect(jsonPath("$.schedule.timeSlots[1].endTime").value("19:00"));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).findById(id);
    }

    // Use-case: Пользователь хочет найти несуществующую секцию по некоторому id.
    @Test
    void testFindById_NotFound() throws Exception {
        // Given: Хотим найти несуществующую секцию с заданным id.
        // Given: Мокаем поведение сервиса при отсутствии секции.
        int id = 2;
        when(service.findById(id)).thenThrow(NoSuchElementException.class);

        // When: Выполняем GET-запрос на поиск несуществующей секции с заданным id.
        mockMvc.perform(get("/api/sections/" + id))

                //Then: Ожидаем код ответа 404
                .andExpect(status().isNotFound());
        // Then: Проверяем, что метод действительно вызывался 1 раз
        verify(service, times(1)).findById(id);
    }

    // Use-case: Пользователь хочет найти существующую секцию по некоторому названию.
    @Test
    void testFindByName_Success() throws Exception {
        // Given: У нас есть желаемая секция, её дто, известный id.
        // Given: Замокано поведение сервиса, возвращает нужную секцию по заданному названию.
        Section section = createSection("Плавание", 1, "Бассейн НГУ", "Тимофеев С. И.");
        SectionDTO sectionDTO = SectionConverter.sectionToDTO(section);
        String name = section.getName();
        when(service.findByName(name)).thenReturn(section);

        // When: Выполняем GET-запрос на поиск секции с заданным названием.
        mockMvc.perform(get("/api/sections/name/" + name))

                // Then: Ожидаем, что секция будет найдена.
                .andExpect(status().isOk())
                // Then: Проверяем, что найденная секция действительно соответствует искомой.
                .andExpect(jsonPath("$.name").value(sectionDTO.getName()))
                .andExpect(jsonPath("$.teacher").value(sectionDTO.getTeacher()))
                .andExpect(jsonPath("$.place").value(sectionDTO.getPlace()))
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(2)));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).findByName(name);
    }

    // Use-case: Пользователь хочет найти несуществующую секцию по некоторому названию.
    @Test
    void testFindByName_NotFound() throws Exception {
        // Given: Есть название несуществующей секции, мокаем поведение сервиса при отсутствии секции.
        String name = "Название несуществующей секции";
        when(service.findByName(name)).thenThrow(NoSuchElementException.class);

        // When: Выполняем GET-запрос на поиск несуществующей секции.
        mockMvc.perform(get("/api/sections/name/" + name))

                // Then: Ожидаем код ответа 404.
                .andExpect(status().isNotFound());
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).findByName(name);
    }

    // Use-case: Пользователь хочет найти все секции.
    @Test
    void testFindAllSections() throws Exception {
        // Given: У нас есть две секции, которые пользователь должен получить при запросе всех секций.
        // Given: Также есть их дто, замокано поведение сервиса.
        Section section1 = createSection("Плавание", 1, "Бассейн НГУ", "Тимофеев С. И.");
        Section section2 = createSection("Футбол", 2, "Стадион, малый игровой зал", "Мезенцев С. В.");
        SectionDTO section1DTO = SectionConverter.sectionToDTO(section1);
        SectionDTO section2DTO = SectionConverter.sectionToDTO(section2);
        when(service.findAllSections()).thenReturn(List.of(section1, section2));

        // When: Выполняем GET-запрос на получение списка всех секций.
        mockMvc.perform(get("/api/sections"))

                // Then: Ожидаем успешное выполнение запроса.
                .andExpect(status().isOk())
                // Then: Проверяем, что нашлось действительно 2 секции.
                .andExpect(jsonPath("$", hasSize(2)))
                // Then: Проверяем, что обе найденные секции имеют нужные названия.
                .andExpect(jsonPath("$[0].name").value(section1DTO.getName()))
                .andExpect(jsonPath("$[1].name").value(section2DTO.getName()));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).findAllSections();
    }

    // Use-case: Пользователь хочет создать секцию.
    @Test
    void testCreateSection_Success() throws Exception {
        // Given: У нас есть новая секция, которую пользователь хочет создать.
        // Given: Мокаем поведение сервиса при запросе на создание секции.
        String name = "Футбол", place = "стадион, малый игровой зал", teacher = "Мезенцев С. В.";
        Section newSection = createSection(name, 1, place, teacher);
        when(service.createSection(any(SectionDTO.class))).thenReturn(newSection);

        // When: Выполняем POST-запрос на создание секции.
        mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                     "name": "Футбол",
                                     "teacher": "Мезенцев С. В.",
                                     "place": "стадион, малый игровой зал",
                                     "schedule": {
                                         "timeSlots": [
                                             {
                                                 "dayOfWeek": "FRIDAY",
                                                 "startTime": "18:00",
                                                 "endTime": "19:00"
                                             }, {
                                                 "dayOfWeek": "WEDNESDAY",
                                                 "startTime": "18:00",
                                                 "endTime": "19:00"
                                             }
                                         ]
                                     }
                                 }
                                """))

                // Then: Ожидаем успешный ответ.
                .andExpect(status().isOk())
                // Then: Проверяем данные созданной секции на совпадение с данными новой.
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.teacher").value(teacher))
                .andExpect(jsonPath("$.place").value(place))
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(2)));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).createSection(any(SectionDTO.class));
    }

    // Use-case: Пользователь хочет обновить секцию (новое название).
    @Test
    void testUpdateSection_Name() throws Exception {
        // Given: У нас есть новое название секции и старая версия секции.
        // Given: Мокаем поведение сервиса при обновлении секции.
        String name = "Футбол", place = "стадион, малый игровой зал", teacher = "Мезенцев С. В.";
        Section section = createSection(name, 1, place, teacher);
        section.setName("Баскетбол (мужской)");
        when(service.updateSection(eq(1), any(SectionDTO.class))).thenReturn(section);

        // When: Выполняем PUT-запрос на обновление секции.
        mockMvc.perform(put("/api/sections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Баскетбол (мужской)"
                                }
                                """))

                // Then: Ожидаем успешное выполнение.
                .andExpect(status().isOk())
                // Then: Проверяем, что название обновилось.
                .andExpect(jsonPath("$.name").value("Баскетбол (мужской)"));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).updateSection(eq(1), any(SectionDTO.class));
    }

    // Use-case: Пользователь хочет обновить секцию (новое место и учитель).
    @Test
    void testUpdateSection_PlaceTeacher() throws Exception {
        // Given: У нас есть новый учитель, новое место и старая версия секции.
        // Given: Мокаем поведение сервиса при обновлении секции.
        String name = "Футбол", place = "стадион, малый игровой зал", teacher = "Мезенцев С. В.";
        Section section = createSection(name, 1, place, teacher);
        String newTeacher = "Шумейко Д.В.", newPlace = "Большой игровой зал";
        section.setTeacher(newTeacher);
        section.setPlace(newPlace);
        when(service.updateSection(eq(1), any(SectionDTO.class))).thenReturn(section);

        // When: Выполняем PUT-запрос на обновление секции.
        mockMvc.perform(put("/api/sections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "place": "Большой игровой зал",
                                  "teacher": "Шумейко Д.В."
                                }
                                """))

                // Then: Ожидаем успешное выполнение.
                .andExpect(status().isOk())
                // Then: Проверяем, что учитель и место обновились.
                .andExpect(jsonPath("$.teacher").value(newTeacher))
                .andExpect(jsonPath("$.place").value(newPlace));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).updateSection(eq(1), any(SectionDTO.class));
    }

    // Use-case: Пользователь хочет удалить секцию по id.
    @Test
    void testDeleteSectionById_Success() throws Exception {
        // Given: Мокаем поведение сервиса при удалении секции.
        when(service.deleteSectionById(1)).thenReturn(true);

        // When: Выполняем DELETE-запрос на удаление секции.
        mockMvc.perform(delete("/api/sections/1"))
                // Then: Ожидаем успешное выполнение.
                .andExpect(status().isOk());
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).deleteSectionById(1);
    }

    // Use-case: Пользователь хочет удалить несуществующую секцию по id.
    @Test
    void testDeleteSectionById_NotFound() throws Exception {
        // Given: Мокаем поведение сервиса при удалении секции.
        when(service.deleteSectionById(1)).thenReturn(false);

        // When: Выполняем DELETE-запрос на удаление секции.
        mockMvc.perform(delete("/api/sections/1"))

                // Then: Ожидаем код ответа 404.
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"Section with id '1' does not exist\"}"));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).deleteSectionById(1);
    }

    // Use-case: Пользователь хочет удалить секцию по названию.
    @Test
    void testDeleteSectionByName_Success() throws Exception {
        // Given: Мокаем поведение сервиса при удалении секции.
        when(service.deleteSectionByName("name")).thenReturn(true);

        // When: Выполняем DELETE-запрос на удаление секции.
        mockMvc.perform(delete("/api/sections/name/name"))

                // Then: Ожидаем успешное выполнение.
                .andExpect(status().isOk());
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).deleteSectionByName("name");
    }

    // Use-case: Пользователь хочет удалить несуществующую секцию по названию.
    @Test
    void testDeleteSectionByName_NotFound() throws Exception {
        // Given: Мокаем поведение сервиса при удалении секции.
        when(service.deleteSectionByName("NonExistingSection")).thenReturn(false);

        // When: Выполняем DELETE-запрос на удаление секции.
        mockMvc.perform(delete("/api/sections/name/NonExistingSection"))

                // Then: Ожидаем код ответа 404.
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"Section with name 'NonExistingSection' does not exist\"}"));
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(service, times(1)).deleteSectionByName("NonExistingSection");
    }
}
