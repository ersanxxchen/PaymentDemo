package com.example.demo.project.domain.DO;

import lombok.Data;

@Data
public class Merchant {
    private int merchantId;

    private String companyName;

    private String websiteType;

    private Integer channelId;

    private String channelMid;

    private String channelKey;

    private String channelSecureKey;

    private String merchantSignKey;

    private Channel channel;
}
