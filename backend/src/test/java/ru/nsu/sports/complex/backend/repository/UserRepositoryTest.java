package ru.nsu.sports.complex.backend.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User(
                1,
                "Andrei Grigoriev-Apollonov",
                "andrei.grigoriev-apollonov@rambler.ru");

        when(userRepository.save(user)).thenReturn(new User(
                2, "Andrei Grigoriev-Apollonov", "andrei.grigoriev-apollonov@rambler.ru"));

        User savedUser = userService.create(user);

        assertNotNull(savedUser);
        assertEquals("Andrei Grigoriev-Apollonov", savedUser.getName());
        assertEquals("andrei.grigoriev-apollonov@rambler.ru", savedUser.getEmail());
    }

    @Test
    void testFindByName() {
        Integer userId = 1;
        User user = new User(
                userId,
                "Andrei Grigoriev-Apollonov",
                "andrei.grigoriev-apollonov@rambler.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.find(userId);

        assertNotNull(foundUser);
        assertEquals("Andrei Grigoriev-Apollonov", foundUser.getName());
        assertEquals("andrei.grigoriev-apollonov@rambler.ru", foundUser.getEmail());
    }
}

