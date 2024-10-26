package ru.nsu.sports.complex.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/users")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @Operation(summary = "Создать список созданных пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = List.class))
            })})
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

    @Operation(summary = "Получить информацию о пользователе по Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDTO.class))
            })})
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

    @Operation(summary = "Создать нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Созданный пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDTO.class))
            })})
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

    @Operation(summary = "Обновить данные существующего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленный пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDTO.class))
            })})
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

    @Operation(summary = "Удалить секцию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = Void.class))
            })})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (userService.delete(id))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
