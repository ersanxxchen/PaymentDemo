package com.example.demo.project.domain.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Channel {

    private Integer channelId;

    private String channelName;

    private String bank;

    private String method;

    private BigDecimal dayLimit;

    private BigDecimal balanceThreshold;

    private String status;

}
