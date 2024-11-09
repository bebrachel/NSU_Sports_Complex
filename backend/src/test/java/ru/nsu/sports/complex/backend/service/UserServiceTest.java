package ru.nsu.sports.complex.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.repository.UserRepository;
import ru.nsu.sports.complex.backend.service.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User(null, "Alberto", "mandarino@gmail.com");
        when(userRepository.save(user)).thenReturn(new User(1, "Alberto", "mandarino@gmail.com"));

        User createdUser = userService.create(user);

        assertNotNull(createdUser.getId());
        assertEquals("Alberto", createdUser.getName());
        assertEquals("mandarino@gmail.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindById() {
        Integer userId = 1;
        String userName = "Alberto";
        String userEmail = "mandarino@gmail.com";
        User user = new User(userId, userName, userEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.find(userId);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindByName() {
        String userName = "Alberto";
        User user = new User(1, userName, "alberto@example.com");

        when(userRepository.findByName(userName)).thenReturn(user);

        User result = userService.findByName(userName);

        assertNotNull(result);
        assertEquals(userName, result.getName());
        assertEquals("alberto@example.com", result.getEmail());
        verify(userRepository, times(1)).findByName(userName);
    }

    @Test
    void testFindAllUsers() {
        List<User> users = Arrays.asList(
                new User(1, "Murilo Benício", "murilo.benício@gmail.com"),
                new User(2, "Giovanna Antonelli", "giovanna.antonelli@gmail.com"),
                new User(3, "Vera Fischer", "vera.fischer@gmail.com")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(3, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        Integer userId = 1;
        User updatedUser = new User(userId, "Alberto Updated", "updated@gmail.com");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.update(userId, updatedUser);

        assertEquals("Alberto Updated", result.getName());
        assertEquals("updated@gmail.com", result.getEmail());
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testDeleteUser() {
        Integer userId = 1;

        doNothing().when(userRepository).deleteById(userId);

        boolean isDeleted = userService.delete(userId);

        assertTrue(isDeleted);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteAll() {
        doNothing().when(userRepository).deleteAll();

        boolean isDeleted = userService.deleteAll();

        assertTrue(isDeleted);
        verify(userRepository, times(1)).deleteAll();
    }
}
