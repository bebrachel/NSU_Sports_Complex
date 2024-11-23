package ru.nsu.sports.complex.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {

    @NotBlank(message = "Name must not be blank")
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
