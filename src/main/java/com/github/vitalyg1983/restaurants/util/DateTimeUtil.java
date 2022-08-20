package com.github.vitalyg1983.restaurants.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class DateTimeUtil {

    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    public static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);
    public static final LocalDate NOW_DATE = LocalDate.now();
    public static final LocalDateTime NOW_DATE_TIME = LocalDateTime.now();
    public static final LocalTime VOTE_ELEVEN_TIME = LocalTime.of(11, 0, 0);
    public static LocalTime VOTE_TEST_TIME;

    public static LocalDateTime atStartOfDayOrMin(LocalDate localDate) {
        return localDate != null ? localDate.atStartOfDay() : MIN_DATE;
    }

    public static LocalDateTime atStartOfNextDayOrMax(LocalDate localDate) {
        return localDate != null ? localDate.plus(1, ChronoUnit.DAYS).atStartOfDay() : MAX_DATE;
    }
}