package com.example.demo.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;

public class CardDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(CardDataUtils.class);


    // 基础卡号格式正则
    public static final String BASIC_CARD_NO_REGULAR = "^[0-9]{13,19}";

    /**
     * 数据脱敏
     *
     * @param data 待脱敏数据 为空则直接返回
     * @param prefix 前面保留几位 小于0则原值返回
     * @param suffix 后面保留几位 小于0则原值返回
     * @return 脱敏后数据
     */
    public static String dataDesensitization(String data, int prefix, int suffix) {
        if (StringUtils.isEmpty(data) || prefix < 0 || suffix < 0) {
            return "";
        }
        if(data.length() < prefix+suffix) {
            return data;
        }
        char[] chars = data.toCharArray();
        for (int i = prefix; i < chars.length - suffix; i++) {
            chars[i] = '*';
        }
        return new String(chars);
    }

    public static boolean checkCardNo(String cardNo) {
        try {
            if (StringUtils.isBlank(cardNo)) {
                logger.info("卡号空白");
                return false;
            }
            if (!cardNo.matches(BASIC_CARD_NO_REGULAR)) {
                logger.info("13~19位数字校验不通过");
                return false;
            }
            return CardDataUtils.isValidCreditCardNumber(cardNo);
        } catch (Exception e) {
            logger.error("校验卡号异常", e);
            return false;
        }

    }

    public static boolean checkExpirationDate(String expireDate) {
        if (StringUtils.isBlank(expireDate)) {
            return false;
        }
        // 解析有效期字符串为月份和年份
        String[] parts = expireDate.split("/");
        if (parts.length < 2) {
            return false;
        }
        if (!StringUtils.isNumeric(parts[0]) || !StringUtils.isNumeric(parts[1])) {
            return false;
        }

        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        // 处理2位年份传参-仅支持到2099年
        if (String.valueOf(year).length() == 2) {
            year = 2000 + year;
        }
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 创建有效截止日期
        LocalDate expiryDate = LocalDate.of(year, month, currentDate.getDayOfMonth());

        // 比较当前日期与有效截止日期
        return currentDate.isBefore(expiryDate) || currentDate.isEqual(expiryDate);
    }

    public static boolean isValidCreditCardNumber(String cardNumber) {
        // int array for processing the cardNumber
        int[] cardIntArray = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            cardIntArray[i] = Integer.parseInt("" + c);
        }
        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;  // step 1
            if (num > 9) {
                num = num % 10 + num / 10;  // step 2
            }
            cardIntArray[i] = num;
        }
        int sum = Arrays.stream(cardIntArray).sum();  // step 3
        boolean result = (sum % 10 == 0);
        logger.info("luhn算法计算结果:" + result);
        return result;
    }

}
