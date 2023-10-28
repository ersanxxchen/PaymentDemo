package com.example.demo.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankReturn {

    @Builder.Default
    private String transStatus = "";

    @Builder.Default
    private String bankReturnCode = "";

    @Builder.Default
    private String bankReturnInfo = "";

    @Builder.Default
    private String bankOrderNo = "";
}
