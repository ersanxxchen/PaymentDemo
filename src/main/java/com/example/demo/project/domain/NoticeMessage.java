package com.example.demo.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeMessage {

    private String orderMerNo = "";

    private String orderStatus = "";

    private String orderNo = "";

    private String orderAmount = "";

    private String orderCurrency = "";

    private String orderResponseCode = "";

    private String orderInstructions = "";
}
