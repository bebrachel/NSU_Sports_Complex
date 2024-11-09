package ru.nsu.sports.complex.backend.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.sports.complex.backend.model.User;

import java.util.Set;

@Getter
@Setter
public class SectionDTO {
    private String name;
    private String teacher;
    private String place;
    private String schedule;
    private Integer capacity;
    private Set<User> users;
}
