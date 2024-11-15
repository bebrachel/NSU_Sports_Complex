package ru.nsu.sports.complex.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionDTO {
    public SectionDTO() {
    }

    public SectionDTO(String name, String teacher, String place, String schedule) {
        this.name = name;
        this.teacher = teacher;
        this.place = place;
        this.schedule = schedule;
    }

    private String name;
    private String teacher;
    private String place;
    private String schedule;
}
