package ru.nsu.sports.complex.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.sports.complex.backend.converter.UserConverter;
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.repository.UserRepository;
import ru.nsu.sports.complex.backend.service.impl.UserServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    private User user1;
    private User newUser;

    @BeforeEach
    void setup() {
        // Создание тестовых данных
        user1 = new User();
        user1.setId(1);
        user1.setName("Kolya");
        user1.setEmail("n.valikov@g.nsu.ru");
        user1.setPassword("qwerty");

        newUser = new User();
        newUser.setId(2);
        newUser.setName("Pivo");
        newUser.setEmail("gudvin0203@gmail.com");
        newUser.setPassword("1234");
    }

    private void equalsUsers(User user1, User user2) {
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getUsername(), user2.getUsername());
    }

    @Test
    void testFindById_Success() {
        Integer id = user1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(user1));

        User result = service.findById(id);
        equalsUsers(result, user1);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        Integer id = user1.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findById(id));

        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByName_Success() {
        String name = user1.getName();
        when(repository.findByName(name)).thenReturn(user1);

        User result = service.findByName(name);
        equalsUsers(result, user1);
        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testFindByName_NotFound() {
        String name = user1.getName();
        when(repository.findByName(name)).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findByName(name));

        verify(repository, times(1)).findByName(name);
    }

    @Test
    void testFindAllUsers() {
        when(repository.findAll()).thenReturn(List.of(user1));

        assertEquals(1, service.findAllUsers().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testCreateUser() {
        when(repository.save(any())).thenReturn(newUser);

        User result = service.createUser(newUser);
        equalsUsers(result, newUser);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdateUser_Success() {
        Integer id = user1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(user1));
        when(repository.save(any())).thenReturn(user1);

        UserDTO newUserDTO = UserConverter.userToDTO(newUser);
        User updatedUser = service.updateUser(id, newUserDTO);

        assertNotNull(updatedUser);
        assertEquals(updatedUser.getName(), newUser.getName());
        assertEquals(updatedUser.getEmail(), newUser.getEmail());
        assertEquals(updatedUser.getPassword(), newUser.getPassword());
        assertEquals(updatedUser.getUsername(), newUser.getUsername());

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdateUser_NotFound() {
        Integer id = user1.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        UserDTO newUserDTO = UserConverter.userToDTO(newUser);
        assertThrows(NoSuchElementException.class, () -> service.updateUser(id, newUserDTO));

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteUserById_Success() {
        Integer id = user1.getId();
        when(repository.findById(id)).thenReturn(Optional.of(user1));

        assertTrue(service.deleteUserById(id));

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).delete(user1);
    }

    @Test
    void testDeleteUserById_NotFound() {
        Integer id = user1.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertFalse(service.deleteUserById(id));

        verify(repository, times(1)).findById(id);
        verify(repository, never()).delete(any(User.class));
    }

    @Test
    void testDeleteUserByName_Success() {
        String name = user1.getName();
        when(repository.findByName(name)).thenReturn(user1);

        assertTrue(service.deleteUserByName(name));

        verify(repository, times(1)).findByName(name);
        verify(repository, times(1)).delete(user1);
    }

    @Test
    void testDeleteUserByName_NotFound() {
        String name = user1.getName();
        when(repository.findByName(name)).thenReturn(null);

        assertFalse(service.deleteUserByName(name));

        verify(repository, times(1)).findByName(name);
        verify(repository, never()).delete(any(User.class));
    }
}
