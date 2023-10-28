package com.example.demo.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String currentDateTime(){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return (currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + currentTime.format(DateTimeFormatter.ofPattern("HHmmssSSS")));
    }
}
