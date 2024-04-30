package com.codertq.selleradmin.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * author: coder_tq
 * date: 2024/4/24
 */
public class DateTimeUtil {
    public static LocalDateTime getLocalDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }
    public static LocalDate getLocalDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDate();
    }
}
