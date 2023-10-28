package com.example.demo.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PaymentUtils {

    /**
     * 生成支付ID（含正式及挡掉）
     */
    public static String transNoCreate() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String currentDateTime = (currentDate.format(DateTimeFormatter.ofPattern("yyMMdd")) + currentTime.format(DateTimeFormatter.ofPattern("HHmmssSS")));
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        return currentDateTime + String.format("%06d", randomNumber);
    }

}
