package com.example.demo.project.bank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.project.domain.BankReturn;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.PaymentRequest;
import com.example.demo.project.service.HttpClientService;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AutBank {

    private static final Logger logger = LoggerFactory.getLogger(AutBank.class);

    public BankReturn deal(PaymentRequest paymentRequest, NormalTransaction transaction) {
        BankReturn response = new BankReturn();
        String json = getRequestJson(paymentRequest, transaction);
        try {
            String result = HttpClientService.sendPost("https://api.authorize.net/xml/v1/request.api", json);
            logger.info("订单号:{" + transaction.getTransNo() + "}交易请求结果：" + result);

            response.setTransStatus("P");
            if (StringUtils.isBlank(result) || Objects.equals(result, "cancel")) {
                // 用户取消交易
                response.setBankReturnCode(result);
                response.setBankReturnInfo("用户取消交易");
                response.setTransStatus("F");
                return response;
            }

            JSONObject resultObject = JSON.parseObject(result);
            JSONObject transactionResponse = (JSONObject) resultObject.get("transactionResponse");
            JSONObject message = (JSONObject) resultObject.get("messages");
            String responseCode = (String) message.get("resultCode");
            // 银行返回错误
            if ("Error".equalsIgnoreCase(responseCode)) {
                JSONArray messages = (JSONArray) message.get("message");
                String code = (String) messages.getJSONObject(0).get("code");
                String description = (String) messages.getJSONObject(0).get("text");
                response.setTransStatus("F");
                response.setBankReturnCode(code);
                response.setBankReturnInfo(description);
                return response;
            }
            assert transactionResponse != null;
            String bankTransCode = (String) transactionResponse.get("responseCode");
            String bankNo = (String) transactionResponse.get("transId");
            response.setBankOrderNo(bankNo);
            if ("1".equals(bankTransCode)) {
                JSONArray messages = (JSONArray) transactionResponse.get("messages");
                String code = (String) messages.getJSONObject(0).get("code");
                String description = (String) messages.getJSONObject(0).get("description");
                response.setTransStatus("A");
                response.setBankReturnCode(code);
                response.setBankReturnInfo(description);
            } else {
                JSONArray errors = (JSONArray) transactionResponse.get("errors");
                String errorCode = (String) errors.getJSONObject(0).get("errorCode");
                String errorText = (String) errors.getJSONObject(0).get("errorText");
                response.setTransStatus("F");
                response.setBankReturnCode(errorCode);
                response.setBankReturnInfo(errorText);
            }
        } catch (Exception e) {
            logger.error("请求银行异常", e);
            response.setTransStatus("F");
            response.setBankReturnCode("Fail");
            response.setBankReturnInfo(e.getMessage());
        }
        return response;
    }

    private String getRequestJson(PaymentRequest paymentRequest, NormalTransaction transaction) {
        JSONObject createTransactionRequest = new JSONObject(true);
        Merchant merchant = transaction.getMerchant();
        // Create object with merchant authentication details
        MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
        merchantAuthenticationType.setName(merchant.getChannelMid());
        merchantAuthenticationType.setTransactionKey(merchant.getChannelKey());
        createTransactionRequest.put("merchantAuthentication", merchantAuthenticationType);


        // Populate the payment data
        Map<String, Object> paymentMap = new HashMap<>();
        JSONObject creditCard = new JSONObject(true);
        String[] split = paymentRequest.getA2().split("/");
        String month = split[0];
        String year = split[1];
        if (year.length() == 2) {
            year = "20" + year;
        }
        String expirationDate = year + "-" + month;
        creditCard.put("cardNumber", paymentRequest.getA1());
        creditCard.put("expirationDate", expirationDate);
        creditCard.put("cardCode", paymentRequest.getA3());
        paymentMap.put("creditCard", creditCard);

        // billing
        JSONObject billingTo = new JSONObject(true);
        billingTo.put("firstName", paymentRequest.getBillingFirstName());
        billingTo.put("lastName", paymentRequest.getBillingLastName());
        billingTo.put("company", StringUtils.substring(paymentRequest.getBillingEmail(), 1, 50));
        billingTo.put("address", StringUtils.substring(paymentRequest.getBillingAddress(), 1, 60));
        billingTo.put("city", paymentRequest.getBillingCity());
        billingTo.put("state", paymentRequest.getBillingState());
        billingTo.put("zip", paymentRequest.getBillingZip());
        billingTo.put("country", paymentRequest.getBillingCountry());
        // shipping
        JSONObject shippingTo = new JSONObject(true);
        shippingTo.put("firstName", paymentRequest.getShippingFirstName());
        shippingTo.put("lastName", paymentRequest.getShippingLastName());
        shippingTo.put("company", StringUtils.substring(paymentRequest.getShippingEmail(), 1, 50));
        shippingTo.put("address", StringUtils.substring(paymentRequest.getShippingAddress(), 1, 60));
        shippingTo.put("city", paymentRequest.getShippingCity());
        shippingTo.put("state", paymentRequest.getShippingState());
        shippingTo.put("zip", paymentRequest.getShippingZip());
        shippingTo.put("country", paymentRequest.getShippingCountry());

        JSONObject order = new JSONObject(true);
        order.put("invoiceNumber", StringUtils.substring(transaction.getTransNo(), 2, transaction.getTransNo().length() - 1));

        JSONObject transactionRequestMap = new JSONObject(true);
        transactionRequestMap.put("transactionType", TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
        transactionRequestMap.put("amount", paymentRequest.getOrderAmount().setScale(2, RoundingMode.CEILING).toString());
        transactionRequestMap.put("payment", paymentMap);
        transactionRequestMap.put("order", order);
        transactionRequestMap.put("billTo", billingTo);
        transactionRequestMap.put("shipTo", shippingTo);
        transactionRequestMap.put("customerIP", paymentRequest.getCustomerIP());

        createTransactionRequest.put("refId", StringUtils.substring(transaction.getTransNo(), 2, transaction.getTransNo().length() - 1));
        createTransactionRequest.put("transactionRequest", transactionRequestMap);
        JSONObject sendMap = new JSONObject(true);
        sendMap.put("createTransactionRequest", createTransactionRequest);
        String json = JSON.toJSONString(sendMap);
        String logStr = json.replaceAll(paymentRequest.getA1(), "******").replaceAll(expirationDate, "******").replaceAll(paymentRequest.getA3(), "***");
        logger.info("订单号:{" + transaction.getTransNo() + "}交易请求参数：" + logStr);
        return json;
    }
}
