package ru.nsu.sports.complex.backend.converter;

import ru.nsu.sports.complex.backend.dto.ScheduleDTO;
import ru.nsu.sports.complex.backend.model.Schedule;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ScheduleConverter {
    private ScheduleConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Schedule dtoToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        if (scheduleDTO == null) {
            scheduleDTO = new ScheduleDTO(new ArrayList<>());
        }
        schedule.setTimeSlots(
                scheduleDTO.getTimeSlots().stream()
                        .map(TimeSlotConverter::dtoToTimeSlot)
                        .collect(Collectors.toCollection(ArrayList::new)));
        return schedule;
    }

    public static ScheduleDTO scheduleToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        if (schedule == null) {
            schedule = new Schedule();
            schedule.setTimeSlots(new ArrayList<>());
        }
        scheduleDTO.setTimeSlots(
                schedule.getTimeSlots().stream()
                        .map(TimeSlotConverter::timeSlotToDTO)
                        .toList());
        return scheduleDTO;
    }
}
