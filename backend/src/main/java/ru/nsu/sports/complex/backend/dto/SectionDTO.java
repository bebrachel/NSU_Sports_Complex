package ru.nsu.sports.complex.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.nsu.sports.complex.backend.model.Member;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {

    @NotBlank
    private String name;

    private Integer id;
    private String teacher;
    private String place;
    private Integer capacity;
    private ScheduleDTO schedule;
    private Set<Member> members = new HashSet<>();
}
