package ru.nsu.sports.complex.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (TimeSlot ts : timeSlots) {
            buffer.append(ts);
        }
        return "Schedule: id " + id + " " +
                "TimeSlots " + buffer;
    }
}
