package ru.nsu.sports.complex.backend.service;

import ru.nsu.sports.complex.backend.model.User;
import java.util.List;

public interface UserService {
    User find(Integer id);
    User findByName(String name);
    List<User> findAll();
    User create(User object);
    User update(Integer id, User object);
    boolean delete(Integer id);
    boolean deleteAll();
}
