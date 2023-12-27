package com.example.demo.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.project.bank.AutBank;
import com.example.demo.project.bank.BoaBank;
import com.example.demo.project.bank.CashAppBank;
import com.example.demo.project.bank.TestBank;
import com.example.demo.project.bank.VenmoBank;
import com.example.demo.project.domain.BankReturn;
import com.example.demo.project.domain.DO.Channel;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.NoticeMessage;
import com.example.demo.project.domain.PaymentRequest;
import com.example.demo.project.service.HttpClientService;
import com.example.demo.project.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {


    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private BoaBank boaBank;

    @Autowired
    private AutBank autBank;

    @Autowired
    private TestBank testBank;

    @Autowired
    private VenmoBank venmoBank;

    @Autowired
    private CashAppBank cashAppBank;

    public BankReturn deal(PaymentRequest paymentRequest, NormalTransaction transaction) {
        BankReturn result = null;
        Merchant merchant = transaction.getMerchant();
        Channel channel = merchant.getChannel();
        if ("BOA".equals(channel.getBank())) {
            result = boaBank.deal(paymentRequest, transaction);
        } else if ("AUT".equals(channel.getBank())) {
            result = autBank.deal(paymentRequest, transaction);
        } else if ("TEST".equals(channel.getBank())){
            result = testBank.deal(paymentRequest, transaction);
        } else if ("Venmo".equals(channel.getBank())) {
            result = venmoBank.deal(paymentRequest, transaction);
        } else if ("Cash App".equals(channel.getBank())) {
            result = cashAppBank.deal(paymentRequest, transaction);
        }
        // 推送商户
        return result;
    }

    public void noticeMerchant(NormalTransaction transaction) {
        if(StringUtils.isBlank(transaction.getNoticeUrl())){
            return;
        }
        Merchant merchant = transaction.getMerchant();
        if (StringUtils.equals(merchant.getWebsiteType(), "Woocommerce")) {// WP网站推送
            try {
                String param = "orderID=" + transaction.getMerOrderNo() + "&tranStatus=" + transaction.getStatus();
                String result = HttpClientService.sendWPPost(transaction.getNoticeUrl(), param);
                logger.info(result);
            } catch (Exception e) {
                logger.error("推送商户异常", e);
            }
        } else {
            try {
                NoticeMessage sendMerchantMessage = NoticeMessage.builder()
                        .orderMerNo(transaction.getMerOrderNo())
                        .orderNo(transaction.getTransNo())
                        .orderStatus(transaction.getStatus())
                        .orderAmount(transaction.getAmount().setScale(2, RoundingMode.HALF_UP).toString())
                        .orderCurrency(transaction.getCurrency())
                        .orderResponseCode(transaction.getResultCode())
                        .orderInstructions(transaction.getResultInfo())
                        .build();
                Map<String, Object> sendMerchantMessageMap = new HashMap<>();
                sendMerchantMessageMap.put("TransResultMessage", sendMerchantMessage);
                String param = JSON.toJSONString(sendMerchantMessageMap);
                String result = HttpClientService.sendPost(transaction.getNoticeUrl(), param);
                logger.info(result);
            } catch (Exception e) {
                logger.error("推送商户异常", e);
            }
        }
    }
}
