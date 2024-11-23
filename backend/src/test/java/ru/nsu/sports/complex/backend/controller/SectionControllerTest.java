package ru.nsu.sports.complex.backend.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.nsu.sports.complex.backend.converter.SectionConverter;
import ru.nsu.sports.complex.backend.dto.SectionDTO;
import ru.nsu.sports.complex.backend.model.Section;
import ru.nsu.sports.complex.backend.service.SectionService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SectionService service;

    @MockBean
    private SectionConverter converter;

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
        section1.setSchedule("расписание");
        section1.setTeacher("Тимофеев С. И.");

        section1DTO = new SectionDTO(section1.getName(), section1.getTeacher(), section1.getPlace(), section1.getSchedule());

        section2 = new Section("New section");
        section2.setPlace("New place");
        section2.setTeacher("New teacher");
        section2.setSchedule("New schedule");
        section2.setId(2);

        section2DTO = new SectionDTO(section2.getName(), section2.getTeacher(), section2.getPlace(), section2.getSchedule());
    }

    @Test
    void testFindById_Success() throws Exception {
        int id = section1.getId();
        when(service.findById(id)).thenReturn(section1);
        when(converter.sectionToDTO(section1)).thenReturn(section1DTO);

        mockMvc.perform(get("/api/sections/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(section1DTO.getName()))
                .andExpect(jsonPath("$.teacher").value(section1DTO.getTeacher()))
                .andExpect(jsonPath("$.place").value(section1DTO.getPlace()))
                .andExpect(jsonPath("$.schedule").value(section1DTO.getSchedule()));

        verify(service, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        int id = section1.getId();
        when(service.findById(id)).thenReturn(null);

        mockMvc.perform(get("/api/sections/" + id))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(id);
    }

    @Test
    void testFindByName_Success() throws Exception {
        String name = section1.getName();
        when(service.findByName(name)).thenReturn(section1);
        when(converter.sectionToDTO(section1)).thenReturn(section1DTO);

        mockMvc.perform(get("/api/sections/name/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(section1DTO.getName()))
                .andExpect(jsonPath("$.teacher").value(section1DTO.getTeacher()))
                .andExpect(jsonPath("$.place").value(section1DTO.getPlace()))
                .andExpect(jsonPath("$.schedule").value(section1DTO.getSchedule()));

        verify(service, times(1)).findByName(name);
    }

    @Test
    void testFindByName_NotFound() throws Exception {
        String name = section1.getName();
        when(service.findByName(name)).thenReturn(null);

        mockMvc.perform(get("/api/sections/name/" + name))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findByName(name);
    }

    @Test
    void testFindAllSections() throws Exception {
        when(service.findAllSections()).thenReturn(List.of(section1, section2));
        when(converter.sectionToDTO(section1)).thenReturn(section1DTO);
        when(converter.sectionToDTO(section2)).thenReturn(section2DTO);

        mockMvc.perform(get("/api/sections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(section1DTO.getName()))
                .andExpect(jsonPath("$[1].name").value(section2DTO.getName()));

        verify(service, times(1)).findAllSections();
    }

    @Test
    void testCreateSection_Success() throws Exception {
        String name = "Футбол", schedule = "Пн. 19:00-20:30", place = "стадион, малый игровой зал", teacher = "Мезенцев С. В.";
        Section section = new Section(name);
        section.setTeacher(teacher);
        section.setPlace(place);
        section.setSchedule(schedule);
        SectionConverter sectionConverter = new SectionConverter();
        SectionDTO sectionDTO = sectionConverter.sectionToDTO(section);
        Section sectionWithId = sectionConverter.DTOtoSection(sectionDTO);
        Assertions.assertNotNull(sectionWithId);
        Assertions.assertNotNull(sectionDTO);
        sectionWithId.setId(3);

        when(converter.DTOtoSection(any(SectionDTO.class))).thenReturn(section);
        when(service.createSection(section)).thenReturn(sectionWithId);
        when(converter.sectionToDTO(sectionWithId)).thenReturn(sectionDTO);

        mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Футбол",
                                  "teacher": "Мезенцев С. В.",
                                  "place": "стадион, малый игровой зал",
                                  "schedule": "Пн. 19:00-20:30"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.teacher").value(teacher))
                .andExpect(jsonPath("$.place").value(place))
                .andExpect(jsonPath("$.schedule").value(schedule));

        verify(service, times(1)).createSection(section);
    }

    @Test
    void testUpdateSection_Name() throws Exception {
        section1.setName("Баскетбол (мужской)");
        when(service.updateSection(any(Section.class), eq(1))).thenReturn(section1);

        mockMvc.perform(put("/api/sections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Баскетбол (мужской)"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Баскетбол (мужской)"));

        verify(service, times(1)).updateSection(any(), eq(1));
    }

    @Test
    void testUpdateSection_PlaceTeacher() throws Exception {
        String teacher = "Шумейко Д.В.", place = "Большой игровой зал";
        section1.setTeacher(teacher);
        section1.setPlace(place);
        when(service.updateSection(any(Section.class), eq(1))).thenReturn(section1);

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

        verify(service, times(1)).updateSection(any(), eq(1));
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
