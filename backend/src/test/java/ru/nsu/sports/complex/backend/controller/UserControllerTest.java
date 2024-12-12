package ru.nsu.sports.complex.backend.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nsu.sports.complex.backend.converter.UserConverter;
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Objects;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserController userController;

    // Given: Подготовительный этап.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Use-case: Пользователь хочет найти всех юзеров.
    @Test
    void testLoadAllUsers() {
        // Given: У нас есть 4 юзера, которых пользователь должен получить при запросе всех юзеров.
        List<User> users = List.of(
                new User(1, "Alexandr Astashenok", "alexandr.astashenok@gmail.com"),
                new User(2, "Alexandr Berdnikov", "alexandr.berdnikov@gmail.com"),
                new User(3, "Alexey Kabanov", "alexey.kabanov@gmail.com"),
                new User(4, "Pavel Artemyev", "pavel.artemyev@gmail.com")
        );
        // Given: Мокаем поведение сервиса.
        when(userService.findAll()).thenReturn(users);

        // When: Выполняем метод на получение списка всех юзеров.
        ResponseEntity<List<User>> response = userController.loadAll();

        // Then: Ожидаем успешное выполнение.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Then: Проверяем, что нашлось действительно 4 юзера.
        assertEquals(4, Objects.requireNonNull(response.getBody()).size());
        // Then: Проверяем, что найденные юзеры имеют нужные имена.
        assertEquals("Alexandr Astashenok", response.getBody().get(0).getName());
        assertEquals("Alexandr Berdnikov", response.getBody().get(1).getName());
        assertEquals("Alexey Kabanov", response.getBody().get(2).getName());
        assertEquals("Pavel Artemyev", response.getBody().get(3).getName());
    }

    // Use-case: Пользователь хочет найти юзера по id.
    @Test
    void testLoadOneUser() {
        // Given: У нас есть желаемый юзер, его дто, известный id.
        User user = new User(1, "Alexandr Astashenok", "alberto@gmail.com");
        UserDTO userDTO = new UserDTO(1, "Alexandr Astashenok", "alberto@gmail.com");
        // Given: Мокаем поведение сервиса, конвертера.
        when(userService.find(1)).thenReturn(user);
        when(userConverter.userToDTO(user)).thenReturn(userDTO);

        // When: Хотим получить юзера с заданным id.
        ResponseEntity<UserDTO> response = userController.loadOne(1);

        // Then: Проверяем, что все успешно и нужный юзер найден.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO.getName(), Objects.requireNonNull(response.getBody()).getName());
    }

    // Use-case: Пользователь хочет создать юзера.
    @Test
    void testCreateUser() {
        // Given: У нас есть новый юзер и его дто.
        User user = new User(null, "Alberto", "alberto@gmail.com");
        UserDTO userDTO = new UserDTO(null, "Alberto", "alberto@gmail.com");
        // Given: Мокаем поведение конвертера, сервиса.
        when(userConverter.DTOtoUser(userDTO)).thenReturn(user);
        when(userService.create(user)).thenReturn(new User(1, "Alberto", "alberto@gmail.com"));
        when(userConverter.userToDTO(any())).thenReturn(new UserDTO(1, "Alberto", "alberto@gmail.com"));

        // When: Выполняем метод на создание юзера.
        ResponseEntity<UserDTO> response = userController.create(userDTO);

        // Then: Ожидаем успешное выполнение.
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getId());
    }

    // Use-case: Пользователь хочет обновить юзера.
    @Test
    void testUpdateUser() {
        // Given: У нас есть новый юзер и его id.
        Integer userId = 1;
        UserDTO userDTO = new UserDTO(userId, "Updated Alberto", "alberto.updated@gmail.com");
        User updatedUser = new User(userId, "Updated Alberto", "alberto.updated@gmail.com");
        // Given: Мокаем поведение сервиса и конвертера при обновлении юзера.
        when(userConverter.DTOtoUser(userDTO)).thenReturn(updatedUser);
        when(userService.update(userId, updatedUser)).thenReturn(updatedUser);
        when(userConverter.userToDTO(updatedUser)).thenReturn(userDTO);

        // When: Выполняем метод на обновление юзера.
        ResponseEntity<UserDTO> response = userController.update(userId, userDTO);

        // Then: Ожидаем успешное выполнение.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Then: Проверяем, что юзер обновился.
        assertEquals("Updated Alberto", response.getBody().getName());
        assertEquals("alberto.updated@gmail.com", response.getBody().getEmail());
        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        verify(userService, times(1)).update(userId, updatedUser);
    }

    // Use-case: Пользователь хочет удалить юзера.
    @Test
    void testDeleteUser() {
        // Given: Мокаем поведение сервиса при удалении юзера.
        when(userService.delete(1)).thenReturn(true);

        // When: Выполняем метод на удаление юзера.
        ResponseEntity<Void> response = userController.delete(1);

        // Then: Проверяем, что метод сервиса действительно вызывался 1 раз.
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
