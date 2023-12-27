package com.example.demo.project.domain.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChannelPayInfo {

    private Integer channelId;
    private String payName;
    private String payEmail;
    private String payBankName;
    private BigDecimal payLimitDay;
    private Integer payCountDay;
    private String payImageUrl;
    private String payUrl;

}
