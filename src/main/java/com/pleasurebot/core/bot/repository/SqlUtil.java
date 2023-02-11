package com.pleasurebot.core.bot.repository;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class SqlUtil {
    public static LocalDateTime getLocalDateTimeOrNull(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else return timestamp.toLocalDateTime();
    }

    public static long secondsBetween(LocalDateTime from, LocalDateTime to) {
        return ChronoUnit.SECONDS.between(from, to);
    }
    public static long hoursBetween(LocalDateTime from, LocalDateTime to) {
        return ChronoUnit.HOURS.between(from, to);
    }
    public static long minutesBetween(LocalDateTime from, LocalDateTime to) {
        return ChronoUnit.MINUTES.between(from, to);
    }
}
