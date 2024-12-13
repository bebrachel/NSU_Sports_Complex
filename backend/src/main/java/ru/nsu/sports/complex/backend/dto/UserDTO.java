package ru.nsu.sports.complex.backend.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.sports.complex.backend.model.Section;

import java.util.Set;

@Setter
@Getter
public class UserDTO {
    public UserDTO() {}

    public UserDTO(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    private Integer id;
    private String name;
    private String email;
    private Set<Section> sections;
}