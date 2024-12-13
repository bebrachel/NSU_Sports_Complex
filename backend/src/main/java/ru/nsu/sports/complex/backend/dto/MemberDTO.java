package ru.nsu.sports.complex.backend.dto;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.sports.complex.backend.model.Section;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class MemberDTO {
    public MemberDTO() {}

    public MemberDTO(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    private Integer id;
    private String name;
    private String email;
    private Set<Section> sections = new HashSet<>();
}