package ru.nsu.sports.complex.backend.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.repository.UserRepository;
import ru.nsu.sports.complex.backend.service.UserService;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByName(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
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
