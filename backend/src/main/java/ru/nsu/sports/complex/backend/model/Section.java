package ru.nsu.sports.complex.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

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
    private Integer capacity;

    @ManyToMany(mappedBy = "sections")

    // The set of users who are registered in this section
    private Set<User> users;
}
