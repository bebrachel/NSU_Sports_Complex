package ru.nsu.sports.complex.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
public class Section {
    public Section(String name) {
        this.name = name;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Section section) {
            return section.id.equals(this.id) &
                    section.name.equals(this.name) &
                    section.teacher.equals(this.teacher) &
                    section.place.equals(this.place) &
                    section.schedule.equals(this.schedule);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @Override
    public String toString() {
        return "Section: id " + id + " " +
                "name " + name + " " +
                "teacher " + teacher + " " +
                "place " + place + " " +
                schedule;
    }
}
