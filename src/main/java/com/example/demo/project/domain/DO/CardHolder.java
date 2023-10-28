package com.example.demo.project.domain.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardHolder {

    @Builder.Default
    private String transNo = "";
    @Builder.Default
    private String merNo = "";
    @Builder.Default
    private String merOrderNo = "";
    @Builder.Default
    private String cardNumber = "";

    @Builder.Default
    private String billingFirstName = "";
    @Builder.Default
    private String billingLastName = "";
    @Builder.Default
    private String billingAddress = "";
    @Builder.Default
    private String billingCity = "";
    @Builder.Default
    private String billingState = "";
    @Builder.Default
    private String billingZip = "";
    @Builder.Default
    private String billingCountry = "";
    @Builder.Default
    private String billingPhone = "";
    @Builder.Default
    private String billingEmail = "";

    @Builder.Default
    private String shippingFirstName = "";
    @Builder.Default
    private String shippingLastName = "";
    @Builder.Default
    private String shippingAddress = "";
    @Builder.Default
    private String shippingCity = "";
    @Builder.Default
    private String shippingState = "";
    @Builder.Default
    private String shippingZip = "";
    @Builder.Default
    private String shippingCountry = "";
    @Builder.Default
    private String shippingPhone = "";
    @Builder.Default
    private String shippingEmail = "";

    @Builder.Default
    private String customerIp = "";

    @Builder.Default
    private String createDateTime = "";
    @Builder.Default
    private String updateDateTime = "";
}
