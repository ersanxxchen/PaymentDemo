package com.example.demo.project.service.impl;

import com.example.demo.common.util.CardDataUtils;
import com.example.demo.common.util.DateUtils;
import com.example.demo.common.util.PaymentUtils;
import com.example.demo.project.controller.PaymentController;
import com.example.demo.project.domain.BankReturn;
import com.example.demo.project.domain.DO.BlockTransaction;
import com.example.demo.project.domain.DO.CardHolder;
import com.example.demo.project.domain.DO.Channel;
import com.example.demo.project.domain.DO.ChannelPayInfo;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.DO.TransTotal;
import com.example.demo.project.domain.PaymentRequest;
import com.example.demo.project.mapper.ChannelMapper;
import com.example.demo.project.mapper.TransactionMapper;
import com.example.demo.project.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {


    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private ChannelMapper channelMapper;

    public BlockTransaction saveBlockTransaction(PaymentRequest request) {

        String transNo = PaymentUtils.transNoCreate();

        CardHolder cardHolder = new CardHolder();
        cardHolder.setTransNo(transNo);
        cardHolder.setMerNo(request.getAccount());
        cardHolder.setMerOrderNo(request.getOrderNo());
        cardHolder.setCardNumber(CardDataUtils.dataDesensitization(request.getA1(),6,4));
        cardHolder.setBillingFirstName(request.getBillingFirstName());
        cardHolder.setBillingLastName(request.getBillingLastName());
        cardHolder.setBillingAddress(request.getBillingAddress());
        cardHolder.setBillingCity(request.getBillingCity());
        cardHolder.setBillingState(request.getBillingState());
        cardHolder.setBillingZip(request.getBillingZip());
        cardHolder.setBillingCountry(request.getBillingCountry());
        cardHolder.setBillingPhone(request.getBillingPhone());
        cardHolder.setBillingEmail(request.getBillingEmail());
        cardHolder.setShippingFirstName(request.getShippingFirstName());
        cardHolder.setShippingLastName(request.getShippingLastName());
        cardHolder.setShippingAddress(request.getShippingAddress());
        cardHolder.setShippingCity(request.getShippingCity());
        cardHolder.setShippingState(request.getShippingState());
        cardHolder.setShippingZip(request.getShippingZip());
        cardHolder.setShippingCountry(request.getShippingCountry());
        cardHolder.setShippingPhone(request.getShippingPhone());
        cardHolder.setShippingEmail(request.getShippingEmail());
        cardHolder.setCustomerIp(request.getCustomerIP());
        cardHolder.setCreateDateTime(DateUtils.currentDateTime());
        cardHolder.setUpdateDateTime(DateUtils.currentDateTime());
        transactionMapper.saveCardHolder(cardHolder);

        BlockTransaction blockTransaction = new BlockTransaction();
        blockTransaction.setTransNo(transNo);
        blockTransaction.setMerNo(request.getAccount());
        blockTransaction.setMerOrderNo(request.getOrderNo());
        blockTransaction.setAmount(request.getOrderAmount());
        blockTransaction.setCurrency(request.getOrderCurrency());
        blockTransaction.setDateTime(DateUtils.currentDateTime());
        blockTransaction.setNoticeUrl(request.getOrderNoticeUrl());
        blockTransaction.setCardHolder(cardHolder);
        transactionMapper.saveBlockTransaction(blockTransaction);
        return blockTransaction;
    }

    public NormalTransaction saveNormalTransaction(BlockTransaction blockTransaction) {
        NormalTransaction normalTransaction = new NormalTransaction();
        normalTransaction.setTransNo(blockTransaction.getTransNo());
        normalTransaction.setMerNo(blockTransaction.getMerNo());
        normalTransaction.setMerOrderNo(blockTransaction.getMerOrderNo());
        normalTransaction.setAmount(blockTransaction.getAmount());
        normalTransaction.setCurrency(blockTransaction.getCurrency());
        normalTransaction.setStatus("P");
        normalTransaction.setChannelId(blockTransaction.getChannelId());
        normalTransaction.setDateTime(DateUtils.currentDateTime());
        normalTransaction.setNoticeUrl(blockTransaction.getNoticeUrl());
        normalTransaction.setCardHolder(blockTransaction.getCardHolder());
        transactionMapper.saveNormalTransaction(normalTransaction);
        transactionMapper.deleteBlockTransaction(blockTransaction.getTransNo());
        return normalTransaction;
    }

    public NormalTransaction updateNormalTransaction(BankReturn bankReturn, NormalTransaction transaction){
        transaction.setResultCode(bankReturn.getBankReturnCode());
        transaction.setResultInfo(bankReturn.getBankReturnInfo());
        transaction.setStatus(bankReturn.getTransStatus());
        transaction.setBankOrderNo(bankReturn.getBankOrderNo());
        transactionMapper.updateNormalTransaction(transaction);
        return transaction;
    }

    public NormalTransaction getVenmoTransaction(String transNo) {
        NormalTransaction normalTransaction = transactionMapper.queryNormalTransaction(transNo);
        if(normalTransaction == null){
            return null;
        }
        Channel channel = channelMapper.queryChannel(normalTransaction.getChannelId());

        if (!StringUtils.equals(channel.getBank(), "Venmo")
            && !StringUtils.equals(channel.getBank(), "Cash App")) {
            return null;
        }
        String date = LocalDateTime.now().format(DateUtils.nowDate);
        String startTime = date + "000000000";
        String endTime = date + "235959999";
        List<ChannelPayInfo> channelPayInfos = channelMapper.queryChannelPayInfo(normalTransaction.getChannelId());
        for (ChannelPayInfo info : channelPayInfos) {
            TransTotal transTotal = transactionMapper.queryTransTotal(info.getPayName(), info.getPayEmail(), info.getPayImageUrl(), startTime, endTime);
            // 当天剩余金额
            BigDecimal surplusAmount = info.getPayLimitDay().subtract(transTotal.getTotalAmount());
            // 当天剩余笔数
            int surplusCount = info.getPayCountDay() - transTotal.getTotalNum();
            if (normalTransaction.getAmount().compareTo(surplusAmount) <= 0 && surplusCount > 0) {
                String randomNo = PaymentUtils.getRandomNo();
                normalTransaction.setBankOrderNo(StringUtils.defaultIfBlank(normalTransaction.getBankOrderNo(), randomNo));
                normalTransaction.setTransPayName(info.getPayName());
                normalTransaction.setTransPayEmail(info.getPayEmail());
                normalTransaction.setTransPayImageUrl(info.getPayImageUrl());
                normalTransaction.setTransPayUrl(info.getPayUrl());
                // 更新交易表信息
                transactionMapper.updateVenmoTransaction(normalTransaction);
                break;
            }
        }
        return normalTransaction;
    }

    public void submitVenmoTransaction(String transNo, String payId) {
        NormalTransaction normalTransaction = transactionMapper.queryNormalTransaction(transNo);
        Channel channel = channelMapper.queryChannel(normalTransaction.getChannelId());

        if (!StringUtils.equals(channel.getBank(), "Venmo")
                && !StringUtils.equals(channel.getBank(), "Cash App")) {
            return ;
        }
        transactionMapper.updateVenmoPayId(transNo,payId);
    }

    public List<NormalTransaction> getAllUnNoticeTransaction(){
        return transactionMapper.queryUnNoticeTransaction();
    }

    public void updateNoticeTransaction(NormalTransaction transaction){
        transactionMapper.updateNotice(transaction);
    }
}
