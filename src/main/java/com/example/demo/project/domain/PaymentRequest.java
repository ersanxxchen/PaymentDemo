package com.example.demo.project.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    //交易基本信息
    //商户账户号
    public String account;

    //卡号
    public  String a1;

    //失效日期 MM/yyyy
    public  String a2;

    //安全码
    public  String a3;

    //商户订单号
    public String orderNo;

    //订单交易金额
    public BigDecimal orderAmount;

    //订单币种
    public String orderCurrency;

    //billing信息
    //firstName
    public  String billingFirstName;

    //lastName
    public  String billingLastName;

    //address
    public  String billingAddress;

    //city
    public  String billingCity;

    //state
    public  String billingState;

    //zip
    public  String billingZip;

    //country
    public  String billingCountry;

    //email
    public  String billingEmail;

    //phone
    public  String billingPhone;

    //shipping信息
    //firstName
    public  String shippingFirstName;

    //lastName
    public  String shippingLastName;

    //address
    public  String shippingAddress;

    //city
    public  String shippingCity;

    //state
    public  String shippingState;

    //zip
    public  String shippingZip;

    //country
    public  String shippingCountry;

    //email
    public  String shippingEmail;

    //phone
    public  String shippingPhone;

    //消费者IP
    public  String customerIP;

    // 签名
    public  String signInfo;

    //交易通知推送地址
    public  String orderNoticeUrl;
}
