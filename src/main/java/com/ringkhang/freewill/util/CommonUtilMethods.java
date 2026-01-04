package com.ringkhang.freewill.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CommonUtilMethods {
    // to convert LocalDateTime to timestamp
    public static Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime){
        return Timestamp.valueOf(localDateTime);
    }
}
