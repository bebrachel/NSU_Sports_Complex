package ru.nsu.sports.complex.backend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.repository.UserRepository;
import ru.nsu.sports.complex.backend.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User findById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + id + " does not exist");
        }
        return user;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByName(user.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    @Transactional
    @Override
    public User updateUser(Integer id, UserDTO updateUserDTO) {
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        if (updateUserDTO.getName() != null) {
            oldUser.setName(updateUserDTO.getName());
        }
        if (updateUserDTO.getEmail() != null) {
            oldUser.setEmail(updateUserDTO.getEmail());
        }
        if (updateUserDTO.getPassword() != null) {
            oldUser.setPassword(updateUserDTO.getPassword());
        }

        return userRepository.save(oldUser);
    }

    @Override
    public User findByName(String username) {
        var user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Transactional
    @Override
    public boolean deleteUserById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteUserByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::findByEmail;
    }

    @Override
    public User getCurrentUser() {
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(userEmail);
    }
}
