package ru.politech.theater.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    private static TimeUtils ourInstance = new TimeUtils();

    public static TimeUtils getInstance() {
        return ourInstance;
    }

    private TimeUtils() {}

    public long getTodayMilli() {
        return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    public long getTomorrowMilli() {
        return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plus(1,ChronoUnit.DAYS).toEpochSecond();
    }

}
