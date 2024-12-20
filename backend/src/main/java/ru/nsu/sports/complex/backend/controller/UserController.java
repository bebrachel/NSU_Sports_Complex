package ru.nsu.sports.complex.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.sports.complex.backend.converter.UserConverter;
import ru.nsu.sports.complex.backend.dto.UserDTO;
import ru.nsu.sports.complex.backend.model.User;
import ru.nsu.sports.complex.backend.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @Operation(summary = "Получить информацию о пользователе по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDTO.class))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        User user = service.findById(id);
        return new ResponseEntity<>(UserConverter.userToDTO(user), HttpStatus.OK);
    }

    @Operation(summary = "Получить информацию о пользователе по названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = User.class))
            })
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<User> findByName(@PathVariable String name) {
        User user = service.findByName(name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Получить список созданных пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = List.class))
            })
    })
    @GetMapping
    public List<UserDTO> findAllUsers() {
        return service.findAllUsers().stream()
                .map(UserConverter::userToDTO)
                .toList();
    }

    @Operation(summary = "Создать нового пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDTO.class))
            })
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO newUserDTO) {

        var passwordEncoder = new BCryptPasswordEncoder();
        var user = new User(newUserDTO.getName(), newUserDTO.getEmail(),
                passwordEncoder.encode(newUserDTO.getEmail()));

        return new ResponseEntity<>(UserConverter.userToDTO(user), HttpStatus.OK);
    }

    @Operation(summary = "Обновить поля пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновлен.", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = UserDTO.class))
            })
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUesr(@PathVariable Integer id, @RequestBody UserDTO updatedUserDTO) {
        User updatedUser = service.updateUser(id, updatedUserDTO);
        return new ResponseEntity<>(UserConverter.userToDTO(updatedUser), HttpStatus.OK);
    }

    @Operation(summary = "Удалить пользователя по id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))
            })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer id) {
        boolean isDeleted = service.deleteUserById(id);
        if (isDeleted) {
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            throw new NoSuchElementException("User with id '" + id + "' does not exist");
        }
    }

    @Operation(summary = "Удалить пользователя по названию.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class))
            })
    })
    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteUserByName(@PathVariable String name) {
        boolean isDeleted = service.deleteUserByName(name);
        if (isDeleted) {
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            throw new NoSuchElementException("User with name '" + name + "' does not exist");
        }
    }
}
