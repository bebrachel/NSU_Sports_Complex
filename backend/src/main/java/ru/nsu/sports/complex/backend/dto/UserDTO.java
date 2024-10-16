package ru.nsu.sports.complex.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
}