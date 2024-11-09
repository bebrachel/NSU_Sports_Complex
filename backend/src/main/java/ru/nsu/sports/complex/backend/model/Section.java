package ru.nsu.sports.complex.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String teacher;
    private String place;
    private String schedule;

    @Override
    public String toString() {
        return "id " + this.id +
                "\nname " + this.name +
                "\nteacher " + this.teacher +
                "\nplace " + this.place +
                "\nschedule " + this.schedule;
    }
}
