package com.example.demo.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReturn {

    @Builder.Default
    private String orderNo = "";

    @Builder.Default
    private String transNo = "";

    @Builder.Default
    private String transStatus = "";

    @Builder.Default
    private String transReturnCode = "";

    @Builder.Default
    private String transReturnInfo = "";

    @Builder.Default
    private String directUrl = "";
}
