package at.fhv.matchpoint.partnerservice.utils;

import at.fhv.matchpoint.partnerservice.utils.exceptions.DateTimeFormatException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalDate parseDate(String date) throws DateTimeFormatException {
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (Exception e) {
            throw new DateTimeFormatException();
        }
    }

    public static LocalTime parseTime(String time) throws DateTimeFormatException {
        try {
            return LocalTime.parse(time, timeFormatter);
        } catch (Exception e) {
            throw new DateTimeFormatException();
        }
    }

    public static String formatDate(LocalDate date) throws DateTimeFormatException {
        try {
            return date.format(dateFormatter);
        } catch (Exception e) {
            throw new DateTimeFormatException();
        }
    }

    public static String formatTime(LocalTime time) throws DateTimeFormatException {
        try {
            return time.format(timeFormatter);
        } catch (Exception e) {
            throw new DateTimeFormatException();
        }
    }
}
