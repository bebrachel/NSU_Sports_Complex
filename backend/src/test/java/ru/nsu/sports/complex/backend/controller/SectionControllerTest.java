package ru.nsu.sports.complex.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nsu.sports.complex.backend.configuration.security.JwtAuthenticationFilter;
import ru.nsu.sports.complex.backend.configuration.security.JwtServiceImpl;
import ru.nsu.sports.complex.backend.converter.ScheduleConverter;
import ru.nsu.sports.complex.backend.dto.ScheduleDTO;
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

    private Section section1;
    private SectionDTO section1DTO;
    private Section section2;
    private SectionDTO section2DTO;

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

        ScheduleDTO schedule1DTO = ScheduleConverter.scheduleToDTO(section1.getSchedule());
        section1DTO = new SectionDTO(section1.getName(), section1.getTeacher(), section1.getPlace(), schedule1DTO);

        section2 = new Section("New section");
        section2.setPlace("New place");
        Schedule schedule2 = new Schedule();
        List<TimeSlot> timeSlots2 = new ArrayList<>();
        timeSlots2.add(new TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        timeSlots2.add(new TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0), LocalTime.of(19, 0)));
        schedule2.setTimeSlots(timeSlots2);
        section2.setSchedule(schedule2);
        section2.setTeacher("New teacher");
        section2.setId(2);

        ScheduleDTO schedule2DTO = ScheduleConverter.scheduleToDTO(section2.getSchedule());
        section2DTO = new SectionDTO(section2.getName(), section2.getTeacher(), section2.getPlace(), schedule2DTO);
    }

    @Test
    void testFindById_Success() throws Exception {
        int id = section1.getId();
        when(service.findById(id)).thenReturn(section1);

        mockMvc.perform(get("/api/sections/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(section1DTO.getName()))
                .andExpect(jsonPath("$.teacher").value(section1DTO.getTeacher()))
                .andExpect(jsonPath("$.place").value(section1DTO.getPlace()))
                // Проверяем общее расписание
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(2)))
                // Проверяем первый слот времени
                .andExpect(jsonPath("$.schedule.timeSlots[0].dayOfWeek").value("FRIDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[0].startTime").value("18:00"))
                .andExpect(jsonPath("$.schedule.timeSlots[0].endTime").value("19:00"))
                // Проверяем второй слот времени
                .andExpect(jsonPath("$.schedule.timeSlots[1].dayOfWeek").value("WEDNESDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[1].startTime").value("18:00"))
                .andExpect(jsonPath("$.schedule.timeSlots[1].endTime").value("19:00"));

        verify(service, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        int id = section1.getId();
        when(service.findById(id)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/sections/" + id))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(id);
    }

    @Test
    void testFindByName_Success() throws Exception {
        String name = section1.getName();
        when(service.findByName(name)).thenReturn(section1);

        mockMvc.perform(get("/api/sections/name/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(section1DTO.getName()))
                .andExpect(jsonPath("$.teacher").value(section1DTO.getTeacher()))
                .andExpect(jsonPath("$.place").value(section1DTO.getPlace()))
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(2)));

        verify(service, times(1)).findByName(name);
    }

    @Test
    void testFindByName_NotFound() throws Exception {
        String name = section1.getName();
        when(service.findByName(name)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/sections/name/" + name))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findByName(name);
    }

    @Test
    void testFindAllSections() throws Exception {
        when(service.findAllSections()).thenReturn(List.of(section1, section2));

        mockMvc.perform(get("/api/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(section1DTO.getName()))
                .andExpect(jsonPath("$[1].name").value(section2DTO.getName()));

        verify(service, times(1)).findAllSections();
    }

    @Test
    void testCreateSection_Success() throws Exception {
        String name = "Футбол", place = "стадион, малый игровой зал", teacher = "Мезенцев С. В.";
        Schedule schedule = new Schedule();
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(19, 0), LocalTime.of(20, 0)));
        schedule.setTimeSlots(timeSlots);

        Section section = new Section(name);
        section.setTeacher(teacher);
        section.setPlace(place);
        section.setSchedule(schedule);

        when(service.createSection(any(SectionDTO.class))).thenReturn(section);

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
                                                 "dayOfWeek": "TUESDAY",
                                                 "startTime": "19:00",
                                                 "endTime": "20:00"
                                             }
                                         ]
                                     }
                                 }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.teacher").value(teacher))
                .andExpect(jsonPath("$.place").value(place))
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(1)))
                .andExpect(jsonPath("$.schedule.timeSlots[0].dayOfWeek").value("TUESDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[0].startTime").value("19:00"))
                .andExpect(jsonPath("$.schedule.timeSlots[0].endTime").value("20:00"));

        verify(service, times(1)).createSection(any(SectionDTO.class));
    }

    @Test
    void testUpdateSection_Name() throws Exception {
        section1.setName("Баскетбол (мужской)");
        when(service.updateSection(eq(1), any(SectionDTO.class))).thenReturn(section1);

        mockMvc.perform(put("/api/sections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Баскетбол (мужской)"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Баскетбол (мужской)"));

        verify(service, times(1)).updateSection(eq(1), any(SectionDTO.class));
    }

    @Test
    void testUpdateSection_PlaceTeacher() throws Exception {
        String teacher = "Шумейко Д.В.", place = "Большой игровой зал";
        section1.setTeacher(teacher);
        section1.setPlace(place);
        when(service.updateSection(eq(1), any(SectionDTO.class))).thenReturn(section1);

        mockMvc.perform(put("/api/sections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "place": "Большой игровой зал",
                                  "teacher": "Шумейко Д.В."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacher").value(teacher))
                .andExpect(jsonPath("$.place").value(place));

        verify(service, times(1)).updateSection(eq(1), any(SectionDTO.class));
    }

    @Test
    void testDeleteSectionById_Success() throws Exception {
        when(service.deleteSectionById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/sections/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteSectionById(1);
    }

    @Test
    void testDeleteSectionById_NotFound() throws Exception {
        when(service.deleteSectionById(1)).thenReturn(false);

        mockMvc.perform(delete("/api/sections/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"Section with id '1' does not exist\"}"));
    }

    @Test
    void testDeleteSectionByName_Success() throws Exception {
        when(service.deleteSectionByName("name")).thenReturn(true);

        mockMvc.perform(delete("/api/sections/name/name"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteSectionByName("name");
    }

    @Test
    void testDeleteSectionByName_NotFound() throws Exception {
        when(service.deleteSectionByName("NonExistingSection")).thenReturn(false);

        mockMvc.perform(delete("/api/sections/name/NonExistingSection"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"Section with name 'NonExistingSection' does not exist\"}"));
    }
}
