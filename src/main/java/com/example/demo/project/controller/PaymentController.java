package com.example.demo.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.util.CardDataUtils;
import com.example.demo.framwork.exception.PaymentException;
import com.example.demo.project.domain.BankReturn;
import com.example.demo.project.domain.DO.BlockTransaction;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.PaymentResponse;
import com.example.demo.project.domain.PaymentRequest;
import com.example.demo.project.domain.PaymentReturn;
import com.example.demo.project.service.impl.BaseParamCheckServiceImpl;
import com.example.demo.project.service.impl.MerchantServiceImpl;
import com.example.demo.project.service.impl.PaymentServiceImpl;
import com.example.demo.project.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.project.service.impl.BaseParamCheckServiceImpl.PARAM_ERROR_CODE;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private BaseParamCheckServiceImpl baseParamCheckServiceImpl;

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @Autowired
    private MerchantServiceImpl merchantServiceImpl;

    @Autowired
    private PaymentServiceImpl paymentServiceImpl;

    @PostMapping("/send")
    public PaymentResponse send(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = new PaymentResponse();
        PaymentReturn paymentReturn = new PaymentReturn();
        // 校验传参
        baseParamCheckServiceImpl.checkParam(paymentRequest);
        // 校验商户号有效性
        baseParamCheckServiceImpl.checkAccount(paymentRequest);
        try {
            // save block info
            BlockTransaction blockTransaction = transactionServiceImpl.saveBlockTransaction(paymentRequest);
            paymentReturn.setTransNo(blockTransaction.getTransNo());
            paymentReturn.setOrderNo(blockTransaction.getMerOrderNo());
            // 通道校验
            Merchant merchant = merchantServiceImpl.queryMerchant(Integer.valueOf(blockTransaction.getMerNo()));
            if (merchant.getChannel() == null || !"L".equals(merchant.getChannel().getStatus())) {
                throw new PaymentException(PARAM_ERROR_CODE, "通道无效");
            } else {
                blockTransaction.setChannelId(merchant.getChannelId());
            }
            // save trade
            NormalTransaction normalTransaction = transactionServiceImpl.saveNormalTransaction(blockTransaction);
            normalTransaction.setMerchant(merchant);
            // 发送银行
            BankReturn bankReturn = paymentServiceImpl.deal(paymentRequest, normalTransaction);
            // 处理返回
            normalTransaction = transactionServiceImpl.updateNormalTransaction(bankReturn, normalTransaction);
            // 推送商户
            paymentServiceImpl.noticeMerchant(normalTransaction);
            paymentReturn.setTransStatus(bankReturn.getTransStatus());
            paymentReturn.setTransReturnCode(bankReturn.getBankReturnCode());
            paymentReturn.setTransReturnInfo(bankReturn.getBankReturnInfo());
        } catch (Exception e) {
            logger.error("交易异常", e);
            paymentReturn.setTransStatus("ERROR");
            paymentReturn.setTransReturnCode("ERROR");
            paymentReturn.setTransReturnInfo(e.getMessage());
        }
        paymentResponse.addDto(paymentReturn);
        logger.info("交易结束,{}", JSONObject.toJSONString(paymentResponse));
        return paymentResponse;
    }
}
