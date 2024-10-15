package ru.nsu.sports.complex.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.sports.complex.backend.converter.UserConverter;
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping
    public ResponseEntity<List<User>> loadAll() {
        LOGGER.info("start loadAll users");
        try {
            List<User> users = userService.findAll();
            LOGGER.info("Found {} users", users.size());
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> loadOne(@PathVariable int id) {
        LOGGER.info("start loadOne user by id: {}", id);
        try {
            User user = userService.find(id);
            LOGGER.info("Found: {}", user);
            return new ResponseEntity<>(userConverter.userToDTO(user), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        LOGGER.info("start creating user: {}", userDTO);
        try {
            User user = userService.create(userConverter.DTOtoUser(userDTO));
            return new ResponseEntity<>(userConverter.userToDTO(user), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable int id, @RequestBody UserDTO userDTO) {
        LOGGER.info("start update user: {}", userDTO);
        try {
            User user = userService.update(id, userConverter.DTOtoUser(userDTO));
            return new ResponseEntity<>(userConverter.userToDTO(user), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (userService.delete(id))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
