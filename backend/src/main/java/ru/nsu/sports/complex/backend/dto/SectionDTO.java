package ru.nsu.sports.complex.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private String name;
    private String teacher;
    private String place;
    private String schedule;

    @Override
    public String toString() {
        return "name " + this.name +
                "\nteacher " + this.teacher +
                "\nplace " + this.place +
                "\nschedule " + this.schedule;
    }
}