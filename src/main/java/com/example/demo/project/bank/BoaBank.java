package com.example.demo.project.bank;

import Api.PaymentsApi;
import Invokers.ApiClient;
import Model.CreatePaymentRequest;
import Model.PtsV2PaymentsPost201Response;
import Model.Ptsv2paymentsClientReferenceInformation;
import Model.Ptsv2paymentsDeviceInformation;
import Model.Ptsv2paymentsOrderInformation;
import Model.Ptsv2paymentsOrderInformationAmountDetails;
import Model.Ptsv2paymentsOrderInformationBillTo;
import Model.Ptsv2paymentsOrderInformationLineItems;
import Model.Ptsv2paymentsOrderInformationShipTo;
import Model.Ptsv2paymentsPaymentInformation;
import Model.Ptsv2paymentsPaymentInformationCard;
import Model.Ptsv2paymentsProcessingInformation;
import com.cybersource.authsdk.core.MerchantConfig;
import com.example.demo.framwork.config.BoaConfiguration;
import com.example.demo.project.domain.BankReturn;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.domain.PaymentRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

@Component
public class BoaBank {

    private static final Logger logger = LoggerFactory.getLogger(BoaBank.class);

    public static boolean userCapture = true;

    /**
     * 默认产品信息
     */
    public static final Map<String, String> DEFAULT_PRODUCT_ITEM;

    /**
     * 默认产品名
     */
    public static final String[] ITEM_KEY;

    static {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("Double Line Outline Pens", "22.79");
        productMap.put("50 Color Solid Watercolor Paint Set", "26.96");
        productMap.put("36/48 Giorgione Watercolor Paint Set", "27.99");
        productMap.put("12 Brilliantly Pigmented Colors Set", "37.88");
        productMap.put("Washable Markers", "49.99");
        productMap.put("Wooden Paint Palette", "49.99");
        DEFAULT_PRODUCT_ITEM = Collections.unmodifiableMap(productMap);
        ITEM_KEY = productMap.keySet().toArray(new String[0]);
    }

    public BankReturn deal(PaymentRequest paymentRequest, NormalTransaction transaction) {

        CreatePaymentRequest requestObject = getRequestObject(paymentRequest, transaction);

        PtsV2PaymentsPost201Response result;
        BankReturn response = new BankReturn();
        try {
            Merchant merchant = transaction.getMerchant();
            Properties merchantProp = BoaConfiguration.getMerchantDetails();
            merchantProp.setProperty("merchantID", merchant.getChannelMid());
            merchantProp.setProperty("merchantKeyId", merchant.getChannelKey());
            merchantProp.setProperty("merchantsecretKey", merchant.getChannelSecureKey());
            ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = new MerchantConfig(merchantProp);

            PaymentsApi apiInstance = new PaymentsApi(apiClient);
            logger.info("支付ID:{},BOABank,开始请求银行", transaction.getTransNo());
            result = apiInstance.createPayment(requestObject);
            logger.info("支付ID:{},BOABank,银行响应", transaction.getTransNo());
            String responseCode = apiClient.responseCode;
            String status = apiClient.status;
            logger.info("ResponseCode :" + responseCode);
            logger.info("ResponseMessage :" + status);
            logger.info(result.toString());
            if (Objects.equals(result.getStatus(), "AUTHORIZED")) {
                // 交易成功
                String bankNo = result.getId();
                response.setBankOrderNo(bankNo);// 银行订单号
                response.setTransStatus("A");// 交易状态
                response.setBankReturnCode(result.getStatus());
                response.setBankReturnInfo("Approved");
            } else {
                response.setTransStatus("F");
                response.setBankReturnCode(result.getStatus());
                // 避免无ErrorInformation
                if (result.getErrorInformation() != null) {
                    response.setBankReturnInfo(result.getErrorInformation().getReason() + result.getErrorInformation().getMessage());
                } else {
                    response.setBankReturnInfo("This transaction has been declined.");
                }
            }
        } catch (Exception e) {
            logger.error("请求银行异常", e);
            response.setTransStatus("F");
            response.setBankReturnCode("Fail");
            response.setBankReturnInfo(e.getMessage());
        }
        return response;
    }

    private CreatePaymentRequest getRequestObject(PaymentRequest paymentRequest, NormalTransaction transaction) {
        CreatePaymentRequest requestObj = new CreatePaymentRequest();

        Ptsv2paymentsClientReferenceInformation clientReferenceInformation = new Ptsv2paymentsClientReferenceInformation();
        // 订单号
        clientReferenceInformation.code(StringUtils.substring(transaction.getTransNo(), 2, transaction.getTransNo().length() - 1));
        requestObj.clientReferenceInformation(clientReferenceInformation);

        Ptsv2paymentsProcessingInformation processingInformation = new Ptsv2paymentsProcessingInformation();
        processingInformation.capture(false);
        if (userCapture) {
            processingInformation.capture(true);
        }

        requestObj.processingInformation(processingInformation);
        Ptsv2paymentsDeviceInformation deviceInformation = new Ptsv2paymentsDeviceInformation();
        deviceInformation.ipAddress(paymentRequest.getCustomerIP());
        requestObj.deviceInformation(deviceInformation);

        Ptsv2paymentsPaymentInformation paymentInformation = new Ptsv2paymentsPaymentInformation();
        Ptsv2paymentsPaymentInformationCard paymentInformationCard = new Ptsv2paymentsPaymentInformationCard();
        paymentInformationCard.number(paymentRequest.getA1());
        paymentInformationCard.securityCode(paymentRequest.getA3());
        String[] date = paymentRequest.getA2().split("/");
        String month = date[0];
        String year = date[1];
        if (year.length() == 2) {
            year = "20" + year;
        }
        paymentInformationCard.expirationMonth(month);
        paymentInformationCard.expirationYear(year);
        paymentInformation.card(paymentInformationCard);

        requestObj.paymentInformation(paymentInformation);

        // 订单信息
        Ptsv2paymentsOrderInformation orderInformation = new Ptsv2paymentsOrderInformation();
        Ptsv2paymentsOrderInformationAmountDetails orderInformationAmountDetails = new Ptsv2paymentsOrderInformationAmountDetails();
        orderInformationAmountDetails.totalAmount(paymentRequest.getOrderAmount().toString()); // 金额
        orderInformationAmountDetails.currency(paymentRequest.getOrderCurrency());// 币种
        orderInformation.amountDetails(orderInformationAmountDetails);

        // 账单信息
        Ptsv2paymentsOrderInformationBillTo orderInformationBillTo = new Ptsv2paymentsOrderInformationBillTo();
        orderInformationBillTo.firstName(paymentRequest.getBillingFirstName());
        orderInformationBillTo.lastName(paymentRequest.getBillingLastName());
        orderInformationBillTo.address1(paymentRequest.getBillingAddress());
        orderInformationBillTo.locality(paymentRequest.getBillingCity());
        orderInformationBillTo.postalCode(paymentRequest.getBillingZip());
        orderInformationBillTo.country(paymentRequest.getBillingCountry());
        orderInformationBillTo.email(paymentRequest.getBillingEmail());
        orderInformationBillTo.phoneNumber(paymentRequest.getBillingPhone());
        if ("US".equalsIgnoreCase(paymentRequest.getBillingCountry())) {
            orderInformationBillTo.administrativeArea(paymentRequest.getBillingState());
        }
        orderInformation.billTo(orderInformationBillTo);

        Ptsv2paymentsOrderInformationShipTo orderInformationShipTo = new Ptsv2paymentsOrderInformationShipTo();
        orderInformationShipTo.firstName(paymentRequest.getShippingFirstName());
        orderInformationShipTo.lastName(paymentRequest.getShippingLastName());
        orderInformationShipTo.address1(paymentRequest.getShippingAddress());
        orderInformationShipTo.locality(paymentRequest.getShippingCity());
        orderInformationShipTo.postalCode(paymentRequest.getShippingZip());
        orderInformationShipTo.country(paymentRequest.getShippingCountry());
        if ("US".equalsIgnoreCase(paymentRequest.getShippingCountry())) {
            orderInformationShipTo.administrativeArea(paymentRequest.getShippingState());
        }
        orderInformationShipTo.phoneNumber(paymentRequest.getShippingPhone());
        orderInformation.shipTo(orderInformationShipTo);

        Random random = new Random();   //生成随机下标
        String productName = ITEM_KEY[random.nextInt(ITEM_KEY.length)];
        String productPrice = DEFAULT_PRODUCT_ITEM.get(productName);
        int quantity = paymentRequest.getOrderAmount().divide(new BigDecimal(productPrice), 0, RoundingMode.UP).intValue();
        List<Ptsv2paymentsOrderInformationLineItems> lineItems = new ArrayList<Ptsv2paymentsOrderInformationLineItems>();
        Ptsv2paymentsOrderInformationLineItems lineItem = new Ptsv2paymentsOrderInformationLineItems();
        lineItem.unitPrice(productPrice);
        lineItem.quantity(quantity);
        lineItem.productName(productName);
        lineItems.add(lineItem);
        orderInformation.lineItems(lineItems);

        requestObj.orderInformation(orderInformation);
        return requestObj;
    }
}
