package com.ringkhang.freewill.util;

import com.ringkhang.freewill.helperClasses.TimeUnit;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    // to check if given value is empty or not (Works with Non-primitive types)
    public static boolean isNullEmpty(Object obj){
        if (obj == null) return true;

        if (obj instanceof String) return ((String) obj).isEmpty();

        if (obj instanceof Collection) return ((Collection<?>) obj).isEmpty();

        if (obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();

        if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        if (obj instanceof Optional) return ((Optional<?>) obj).isEmpty();

        // For all other non-primitive objects, consider them "not empty"
        return false;
    }


}