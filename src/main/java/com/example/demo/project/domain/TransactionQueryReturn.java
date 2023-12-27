package com.example.demo.project.domain;

import lombok.Data;


@Data
public class TransactionQueryReturn {


    private String merOrderNo;
    private String amount;
    private String status;
    private String bankOrderNo;
    private String payName;
    private String payEmail;
    private String payImageUrl;
    private String payUrl;
}
