package com.example.demo.common.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PaymentUtils {

    private static final String[] UPPER_CHAR = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
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

    public static String getRandomNo() {
        String millis = String.valueOf(System.currentTimeMillis());
        String str = subStr(millis, 6, 8);
        StringBuilder prefix = new StringBuilder(subStr(str, 1, 4));
        for (int i = 5; i <= 8; i++) {
            String s = subStr(str, i, 1);
            prefix.append(UPPER_CHAR[Integer.parseInt(s)]);
        }
        return String.valueOf(prefix);
    }

    public static String subStr(String s, int pos, int length) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        if (pos < 1) {
            throw new IllegalArgumentException("开始位置必须大于等于1");
        }
        if (length < 1) {
            throw new IllegalArgumentException("长度必须大于等于1");
        }
        int startIndex = pos - 1;
        if (startIndex > s.length()) {
            startIndex = s.length();
        }
        int endIndex = startIndex + length;
        if (endIndex > s.length()) {
            endIndex = s.length();
        }
        return s.substring(startIndex, endIndex);
    }
}
