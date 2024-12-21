package ru.nsu.sports.complex.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member {
    public Member() {
    }

    public Member(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;

    @ManyToMany(mappedBy = "members")
    private Set<Section> sections = new HashSet<>();

    public void enrollInSection(Section section) {
        this.sections.add(section);
        section.getMembers().add(this);
    }

    public void leaveSection(Section section) {
        this.sections.remove(section);
        section.getMembers().remove(this);
    }
}