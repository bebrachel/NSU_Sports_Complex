package ru.nsu.sports.complex.backend;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NsuSportsComplexSystemApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Hello world");
        Assertions.assertTrue(true);
    }

    @Autowired
    private MockMvc mockMvc;

    // Use-case: Пользователь хочет создать новую секцию.
    @Test
    void testAddNewSectionIntegration() throws Exception {
        // Given: Желаемая новая секция.
        String sectionJson = """
                    {
                      "name": "Пауэрлифтинг",
                      "teacher": "Августинович А.С.",
                      "place": "Тренажерный зал",
                      "schedule": {
                        "timeSlots": [
                          {
                            "dayOfWeek": "MONDAY",
                            "startTime": "18:00",
                            "endTime": "21:00"
                          },
                          {
                            "dayOfWeek": "WEDNESDAY",
                            "startTime": "18:00",
                            "endTime": "21:00"
                          },
                          {
                            "dayOfWeek": "FRIDAY",
                            "startTime": "18:00",
                            "endTime": "21:00"
                          }
                        ]
                      }
                    }
                """;

        // When: Выполняем POST-запрос на создание новой секции.
        mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sectionJson))

                // Then: получаем успешное выполнение запроса.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Пауэрлифтинг"))
                .andExpect(jsonPath("$.teacher").value("Августинович А.С."))
                .andExpect(jsonPath("$.place").value("Тренажерный зал"))
                .andExpect(jsonPath("$.schedule.timeSlots", hasSize(3)))
                .andExpect(jsonPath("$.schedule.timeSlots[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[1].dayOfWeek").value("WEDNESDAY"))
                .andExpect(jsonPath("$.schedule.timeSlots[2].dayOfWeek").value("FRIDAY"));
    }

    // Use-case: Пользователь хочет создать нового юзера.
    @Test
    void testAddNewUserIntegration() throws Exception {
        // Given: данные нового юзера.
        String userJson = """
                    {
                      "name": "john doe",
                      "email": "johndoe@example.com"
                    }
                """;

        // When: Выполняем POST-запрос на создание нового юзера.
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))

                // Then: Проверяем успешное выполнение запроса.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("john doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }
}


