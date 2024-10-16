package ru.nsu.sports.complex.backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.repository.UserRepository;
import ru.nsu.sports.complex.backend.service.UserService;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            LOGGER.error("Error creating user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public User find(Integer id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User findByName(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Integer id, User user) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public boolean delete(Integer id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            userRepository.deleteAll();
            return true;
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }
}