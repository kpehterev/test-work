package main.java.com.test.work.util;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;


public final class TimeUtil {

    public static final ZoneId TIME_ZONE = ZoneId.of("UTC");

    private TimeUtil() {
    }

    public static OffsetDateTime nowOffsetDateTime(ZoneId zoneId) {
        return OffsetDateTime.now(zoneId);
    }

    public static Timestamp nowTimestamp(ZoneId zoneId) {
        return convertOffsetDateTimeToTimestamp(nowOffsetDateTime(zoneId));
    }


    public static Timestamp convertOffsetDateTimeToTimestamp(OffsetDateTime offsetDateTime) {
        Instant instant = offsetDateTime.toInstant();
        return Timestamp.from(instant);
    }

    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime, ZoneId zoneId) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Timestamp.from(instant);
    }

    public static Optional<OffsetDateTime> getOffsetDateTimeOfResultSet(ResultSet rs, String columnName, ZoneId zone) {
        try {
            return Optional.of(rs.getTimestamp(columnName).toInstant().atZone(zone).toOffsetDateTime());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
