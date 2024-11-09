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

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadAllUsers() {
        List<User> users = List.of(
                new User(1, "Alexandr Astashenok", "alexandr.astashenok@gmail.com"),
                new User(2, "Alexandr Berdnikov", "alexandr.berdnikov@gmail.com"),
                new User(3, "Alexey Kabanov", "alexey.kabanov@gmail.com"),
                new User(4, "Pavel Artemyev", "pavel.artemyev@gmail.com")
        );
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.loadAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());
        assertEquals("Alexandr Astashenok", response.getBody().get(0).getName());
        assertEquals("Alexandr Berdnikov", response.getBody().get(1).getName());
        assertEquals("Alexey Kabanov", response.getBody().get(2).getName());
        assertEquals("Pavel Artemyev", response.getBody().get(3).getName());
    }

    @Test
    void testLoadOneUser() {
        User user = new User(1, "Alexandr Astashenok", "alberto@gmail.com");
        UserDTO userDTO = new UserDTO(1, "Alexandr Astashenok", "alberto@gmail.com");

        when(userService.find(1)).thenReturn(user);
        when(userConverter.userToDTO(user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.loadOne(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO.getName(), response.getBody().getName());
    }

    @Test
    void testCreateUser() {
        User user = new User(null, "Alberto", "alberto@gmail.com");
        UserDTO userDTO = new UserDTO(null, "Alberto", "alberto@gmail.com");

        when(userConverter.DTOtoUser(userDTO)).thenReturn(user);
        when(userService.create(user)).thenReturn(new User(1, "Alberto", "alberto@gmail.com"));
        when(userConverter.userToDTO(any())).thenReturn(new UserDTO(1, "Alberto", "alberto@gmail.com"));

        ResponseEntity<UserDTO> response = userController.create(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testUpdateUser() {
        Integer userId = 1;
        UserDTO userDTO = new UserDTO(userId, "Updated Alberto", "alberto.updated@gmail.com");
        User updatedUser = new User(userId, "Updated Alberto", "alberto.updated@gmail.com");

        when(userConverter.DTOtoUser(userDTO)).thenReturn(updatedUser);
        when(userService.update(userId, updatedUser)).thenReturn(updatedUser);
        when(userConverter.userToDTO(updatedUser)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.update(userId, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Alberto", response.getBody().getName());
        assertEquals("alberto.updated@gmail.com", response.getBody().getEmail());
        verify(userService, times(1)).update(userId, updatedUser);
    }

    @Test
    void testDeleteUser() {
        when(userService.delete(1)).thenReturn(true);

        ResponseEntity<Void> response = userController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
