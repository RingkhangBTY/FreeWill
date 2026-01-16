package com.ringkhang.freewill.util;

import com.ringkhang.freewill.helperClasses.TimeUnit;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class CommonUtilMethods {

    public CommonUtilMethods() {
    }

    // to convert LocalDateTime to timestamp
    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime){
        Objects.requireNonNull(localDateTime,"localDateTime must not be null");
        return Timestamp.valueOf(localDateTime);
    }

    // find time difference between timestamps and return time difference in minutes
    public static Long timeDifference(LocalDateTime localDateTime, TimeUnit unit){

        Objects.requireNonNull(localDateTime,"local date time can't be null");
        Objects.requireNonNull(unit,"Time unit can't be null");

        LocalDateTime now = LocalDateTime.now();

        return switch (unit){
            case DAY -> ChronoUnit.DAYS.between(localDateTime,now);
            case HOURS -> ChronoUnit.HOURS.between(localDateTime,now);
            case MINUTES -> ChronoUnit.MINUTES.between(localDateTime,now);
            case SECONDS -> ChronoUnit.SECONDS.between(localDateTime,now);
        };
    }

}