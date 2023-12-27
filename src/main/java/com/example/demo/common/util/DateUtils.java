package com.example.demo.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static DateTimeFormatter nowDate = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String currentDateTime(){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return (currentDate.format(nowDate) + currentTime.format(DateTimeFormatter.ofPattern("HHmmssSSS")));
    }

    public static final String getDateAgo(int hours, int minutes,int seconds){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ago = now
                .minus(seconds, ChronoUnit.SECONDS)
                .minus(minutes, ChronoUnit.MINUTES)
                .minus(hours, ChronoUnit.HOURS);
        return ago.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
