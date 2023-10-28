package com.example.demo.project.domain.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NormalTransaction {

    @Builder.Default
    private String transNo = "";
    @Builder.Default
    private String merNo = "";
    @Builder.Default
    private String merOrderNo = "";
    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;
    @Builder.Default
    private String currency = "";
    @Builder.Default
    private String resultCode = "";
    @Builder.Default
    private String resultInfo = "";
    @Builder.Default
    private String status = "";
    @Builder.Default
    private Integer channelId = 0;
    @Builder.Default
    private String dateTime = "";
    @Builder.Default
    private String bankOrderNo = "";
    @Builder.Default
    private String noticeUrl = "";

    @Builder.Default
    private CardHolder cardHolder = null;
    @Builder.Default
    private Merchant merchant = null;
}
