package com.example.demo.project.controller;

import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.PaymentResponse;
import com.example.demo.project.domain.TransactionQueryReturn;
import com.example.demo.project.domain.TransactionRequest;
import com.example.demo.project.domain.TransactionResponse;
import com.example.demo.project.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactino")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @PostMapping("/query")
    public TransactionResponse orderQuery(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = new TransactionResponse();
        NormalTransaction venmoTransaction = transactionServiceImpl.getVenmoTransaction(transactionRequest.getTransNo());
        if (venmoTransaction != null) {
            TransactionQueryReturn transactionQueryReturn = new TransactionQueryReturn();
            transactionQueryReturn.setMerOrderNo(venmoTransaction.getMerOrderNo());
            transactionQueryReturn.setBankOrderNo(venmoTransaction.getBankOrderNo());
            transactionQueryReturn.setStatus(venmoTransaction.getStatus());
            transactionQueryReturn.setAmount(venmoTransaction.getAmount().toString());
            transactionQueryReturn.setPayName(venmoTransaction.getTransPayName());
            transactionQueryReturn.setPayEmail(venmoTransaction.getTransPayEmail());
            transactionQueryReturn.setPayImageUrl(venmoTransaction.getTransPayImageUrl());
            transactionQueryReturn.setPayUrl(venmoTransaction.getTransPayUrl());
            transactionResponse.addDto(transactionQueryReturn);
        }
        return transactionResponse;
    }

    @PostMapping("/submit")
    public TransactionResponse orderSubmit(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionServiceImpl.submitVenmoTransaction(transactionRequest.getTransNo(), transactionRequest.getPayId());
        return transactionResponse;
    }
}
