package com.example.demo.project.bank;

import com.example.demo.project.domain.BankReturn;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class VenmoBank {

    private static final Logger logger = LoggerFactory.getLogger(VenmoBank.class);

    public BankReturn deal(PaymentRequest paymentRequest, NormalTransaction transaction) {
        BankReturn response = new BankReturn();
        response.setBankOrderNo(transaction.getTransNo());
        response.setTransStatus("P");
        return response;
    }

}
