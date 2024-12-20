package ru.nsu.sports.complex.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sections")
@Getter
@Setter
public class Section {
    public Section() {
        this.capacity = 1;
    }

    public Section(String name) {
        this.name = name;
        this.capacity = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String teacher;
    private String place;

    private Integer capacity;

    @ManyToMany
    @JoinTable(
            name = "sections_members",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> members = new HashSet<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    public boolean hasCapacity() {
        return this.members.size() < this.capacity;
    }

    @Override
    public String toString() {
        return "Section: id " + id + " " +
                "name " + name + " " +
                "teacher " + teacher + " " +
                "place " + place + " " +
                schedule;
    }
}
