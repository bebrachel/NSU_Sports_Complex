package ru.nsu.sports.complex.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;

import java.util.List;

public interface UserService {

    User findById(Integer id);

    User findByName(String name);

    User findByEmail(String name);

    List<User> findAllUsers();

    User createUser(User object);

    User updateUser(Integer id, UserDTO updateUserDTO);

    boolean deleteUserById(Integer id);

    boolean deleteUserByName(String name);

    UserDetailsService userDetailsService();

    User getCurrentUser();
}
