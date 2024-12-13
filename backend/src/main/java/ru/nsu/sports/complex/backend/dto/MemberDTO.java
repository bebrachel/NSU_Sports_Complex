package ru.nsu.sports.complex.backend.dto;

import lombok.Getter;
import lombok.Setter;

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