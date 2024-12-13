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
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserService service;

    private User user1;
    private UserDTO user1DTO;
    private User user2;
    private UserDTO user2DTO;

    @BeforeEach
    void setup() {
        // Создание тестовых данных
        user1 = new User();
        user1.setId(1);
        user1.setName("Kolya");
        user1.setEmail("zig@zig.zig");
        user1.setPassword("zig");

        user1DTO = new UserDTO(user1.getId(), user1.getName(), user1.getEmail(),
                user1.getPassword());

        user2 = new User();
        user2.setId(2);
        user2.setName("Tolya");
        user2.setEmail("coq@coq.coq");
        user2.setPassword("coq");

        user2DTO = new UserDTO(user2.getId(), user2.getName(), user2.getEmail(),
                user2.getPassword());
    }

    @Test
    void testFindById_Success() throws Exception {
        int id = user1.getId();
        when(service.findById(id)).thenReturn(user1);

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1DTO.getId()))
                .andExpect(jsonPath("$.name").value(user1DTO.getName()))
                .andExpect(jsonPath("$.email").value(user1DTO.getEmail()))
                .andExpect(jsonPath("$.password").value(user1DTO.getPassword()));

        verify(service, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        int id = user1.getId();
        when(service.findById(id)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(id);
    }

    @Test
    void testFindByName_Success() throws Exception {
        String name = user1.getName();
        when(service.findByName(name)).thenReturn(user1);

        mockMvc.perform(get("/api/users/name/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1DTO.getId()))
                .andExpect(jsonPath("$.name").value(user1DTO.getName()))
                .andExpect(jsonPath("$.email").value(user1DTO.getEmail()))
                .andExpect(jsonPath("$.password").value(user1DTO.getPassword()));

        verify(service, times(1)).findByName(name);
    }

    @Test
    void testFindByName_NotFound() throws Exception {
        String name = user1.getName();
        when(service.findByName(name)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/users/name/" + name))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findByName(name);
    }

    @Test
    void testFindAllUsers() throws Exception {
        when(service.findAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(user1DTO.getName()))
                .andExpect(jsonPath("$[1].name").value(user2DTO.getName()));

        verify(service, times(1)).findAllUsers();
    }

//    @Test
//    void testCreateUser_Success() throws Exception {
//        String name = "Kolyan", email = "zele@zele.zele", password = "zele";
//
//        User user = new User(name, email, password);
//
//        when(service.createUser(any(User.class))).thenReturn(user);
//        mockMvc.perform(post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                                {
//                                     "name": "Kolyan",
//                                     "email": "zele@zele.zele",
//                                     "password": "zelenov"
//                                 }
//                                """))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(name))
//                .andExpect(jsonPath("$.email").value(email))
//                .andExpect(jsonPath("$.password").isNotEmpty());
//
//        verify(service, times(1)).createUser(any(User.class));
//    }

    @Test
    void testUpdateUser_Name() throws Exception {
        user1.setName("Sad Kolyan");
        when(service.updateUser(eq(1), any(UserDTO.class))).thenReturn(user1);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Sad Kolyan"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sad Kolyan"));

        verify(service, times(1)).updateUser(eq(1), any(UserDTO.class));
    }

    @Test
    void testUpdateUser_Password() throws Exception {
        String password = "c++";
        user1.setPassword(password);
        when(service.updateUser(eq(1), any(UserDTO.class))).thenReturn(user1);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "password": "c++"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value(password));

        verify(service, times(1)).updateUser(eq(1), any(UserDTO.class));
    }

    @Test
    void testDeleteUserById_Success() throws Exception {
        when(service.deleteUserById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUserById(1);
    }

    @Test
    void testDeleteUserById_NotFound() throws Exception {
        when(service.deleteUserById(1)).thenReturn(false);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"User with id '1' does not exist\"}"));
    }

    @Test
    void testDeleteUserByName_Success() throws Exception {
        when(service.deleteUserByName("name")).thenReturn(true);

        mockMvc.perform(delete("/api/users/name/name"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUserByName("name");
    }

    @Test
    void testDeleteUserByName_NotFound() throws Exception {
        when(service.deleteUserByName("NonExistingUser")).thenReturn(false);

        mockMvc.perform(delete("/api/users/name/NonExistingUser"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"User with name 'NonExistingUser' does not exist\"}"));
    }
}
