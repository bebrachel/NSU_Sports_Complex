package ru.nsu.sports.complex.backend.converter;

import ru.nsu.sports.complex.backend.dto.TimeSlotDTO;
import ru.nsu.sports.complex.backend.model.TimeSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeSlotConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private TimeSlotConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static TimeSlot dtoToTimeSlot(TimeSlotDTO timeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        try {
            // Предполагается, что фронт отправляет день недели в виде DayOfWeek
            timeSlot.setDayOfWeek(DayOfWeek.valueOf(timeSlotDTO.getDayOfWeek()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid day of week: " + timeSlotDTO.getDayOfWeek());
        }
        try {
            // Думаю, что это тоже пусть фронт отрабатывает
            timeSlot.setStartTime(LocalTime.parse(timeSlotDTO.getStartTime(), formatter));
            timeSlot.setEndTime(LocalTime.parse(timeSlotDTO.getEndTime(), formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Expected HH:mm for startTime/endTime");
        }
        return timeSlot;
    }

    public static TimeSlotDTO timeSlotToDTO(TimeSlot timeSlot) {
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        timeSlotDTO.setDayOfWeek(timeSlot.getDayOfWeek().name());
        timeSlotDTO.setStartTime(timeSlot.getStartTime().format(formatter));
        timeSlotDTO.setEndTime(timeSlot.getEndTime().format(formatter));
        return timeSlotDTO;
    }
}
