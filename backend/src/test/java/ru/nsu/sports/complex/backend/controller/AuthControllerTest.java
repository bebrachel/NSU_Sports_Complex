package ru.nsu.sports.complex.backend.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Use-case: Хотим зарегистрировать нового пользователя.
    @Test
    void testSignUp() throws Exception {
        // Given: Данные нового пользователя представлены в теле запроса.

        // When: Выполняем POST-запрос на создание пользователя.
        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                     "name": "Новый пользователь",
                                     "email": "coolboy@example.com",
                                     "password": "Крутой пароль 123"
                                 }
                                """))

                // Then: Проверяем, что ответ имеет статус 200
                .andExpect(status().isOk())
                // Then: Проверяем, что в ответе есть токен
                .andExpect(jsonPath("$.token").exists())
                // Then: Проверяем, что токен не пустой
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
