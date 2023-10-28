package com.example.demo.project.service.impl;

import com.example.demo.common.util.CardDataUtils;
import com.example.demo.framwork.exception.PaymentException;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.domain.PaymentRequest;
import com.example.demo.project.service.ParamCheckService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BaseParamCheckServiceImpl implements ParamCheckService {

    private static final Logger logger = LoggerFactory.getLogger(BaseParamCheckServiceImpl.class);

    public static final String PARAM_ERROR_CODE = "0000001";

    @Autowired
    private MerchantServiceImpl merchantServiceImpl;

    @Override
    public void checkParam(PaymentRequest request) throws PaymentException {
        String requestStr = request.toString();
        requestStr = requestStr.replaceAll(request.getA1(), "***").
                replaceAll(request.getA2(), "***").
                replaceAll(request.getA3(), "***");
        logger.info("交易传入参数:"+requestStr);
        if (StringUtils.isBlank(request.getAccount())) {
            throw new PaymentException(PARAM_ERROR_CODE, "商户账户号不能为空");
        }

        if (!CardDataUtils.checkCardNo(request.getA1())) {
            throw new PaymentException(PARAM_ERROR_CODE,"卡号校验不通过");
        }

        if (!CardDataUtils.checkExpirationDate(request.getA2())) {
            throw new PaymentException(PARAM_ERROR_CODE,"失效日期校验不通过");
        }

        if (StringUtils.isBlank(request.getA3())) {
            throw new PaymentException(PARAM_ERROR_CODE,"安全码不能为空");
        }

        if (StringUtils.isBlank(request.getOrderNo())) {
            throw new PaymentException(PARAM_ERROR_CODE,"商户订单号不能为空");
        }

        if (request.getOrderNo().length() > 50) {
            throw new PaymentException(PARAM_ERROR_CODE,"商户订单号长度最大为50位");
        }

        if (request.getOrderAmount() == null) {
            throw new PaymentException(PARAM_ERROR_CODE,"订单交易金额不能为空");
        }

        if (request.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentException(PARAM_ERROR_CODE,"订单交易金额必须大于0");
        }

        if (StringUtils.isNotBlank(request.getOrderCurrency()) && !"USD".equals(request.getOrderCurrency())) {
            throw new PaymentException(PARAM_ERROR_CODE,"订单交易币种不支持");
        }
        if(StringUtils.isBlank(request.getOrderCurrency())){
            request.setOrderCurrency("USD");
        }

        if (StringUtils.isBlank(request.getBillingFirstName())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingFirstName不能为空");
        }

        if (request.getBillingFirstName().length() > 50) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingFirstName长度不超过50");
        }

        if (StringUtils.isBlank(request.getBillingLastName())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingLastName不能为空");
        }

        if (request.getBillingLastName().length() > 50) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingLastName长度不超过50");
        }

        if (StringUtils.isBlank(request.getBillingAddress())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingAddress不能为空");
        }

        if (request.getBillingAddress().length() > 200) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingAddress长度不超过200");
        }

        if (StringUtils.isBlank(request.getBillingCity())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingCity不能为空");
        }

        if (request.getBillingCity().length() > 40) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingCity长度不超过40");
        }

        if (StringUtils.isBlank(request.getBillingState())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingState不能为空");
        }

        if (request.getBillingState().length() > 40) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingState长度不超过40");
        }

        if (StringUtils.isBlank(request.getBillingZip())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingZip不能为空");
        }

        if (request.getBillingZip().length() > 20) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingZip长度不超过20");
        }

        if (StringUtils.isBlank(request.getBillingCountry())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingCountry不能为空");
        }

        if (request.getBillingCountry().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingCountry长度不超过60");
        }

        if (StringUtils.isBlank(request.getBillingEmail())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingEmail不能为空");
        }

        if (request.getBillingEmail().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingEmail长度不超过60");
        }

        if (StringUtils.isBlank(request.getBillingPhone())) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingPhone不能为空");
        }

        if (request.getBillingPhone().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"billingPhone长度不超过60");
        }

        if (StringUtils.isBlank(request.getShippingFirstName())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingFirstName不能为空");
        }

        if (request.getShippingFirstName().length() > 50) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingFirstName长度不超过50");
        }

        if (StringUtils.isBlank(request.getShippingLastName())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingLastName不能为空");
        }

        if (request.getShippingLastName().length() > 50) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingLastName长度不超过50");
        }

        if (StringUtils.isBlank(request.getShippingAddress())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingAddress不能为空");
        }

        if (request.getShippingAddress().length() > 200) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingAddress长度不超过200");
        }

        if (StringUtils.isBlank(request.getShippingCity())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingCity不能为空");
        }

        if (request.getShippingCity().length() > 40) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingCity长度不超过40");
        }

        if (StringUtils.isBlank(request.getShippingState())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingState不能为空");
        }

        if (request.getShippingState().length() > 40) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingState长度不超过40");
        }

        if (StringUtils.isBlank(request.getShippingZip())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingZip不能为空");
        }

        if (request.getShippingZip().length() > 20) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingZip长度不超过20");
        }

        if (StringUtils.isBlank(request.getShippingCountry())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingCountry不能为空");
        }

        if (request.getShippingCountry().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingCountry长度不超过60");
        }

        if (StringUtils.isBlank(request.getShippingEmail())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingEmail不能为空");
        }


        if (request.getShippingEmail().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingEmail长度不超过60");
        }

        if (StringUtils.isBlank(request.getShippingPhone())) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingPhone不能为空");
        }

        if (request.getShippingPhone().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"shippingPhone长度不超过60");
        }

        if (StringUtils.isBlank(request.getCustomerIP())) {
            throw new PaymentException(PARAM_ERROR_CODE,"消費者IP不能为空");
        }

        if (request.getCustomerIP().length() > 60) {
            throw new PaymentException(PARAM_ERROR_CODE,"消費者IP长度不超过60");
        }

        if (request.getOrderNoticeUrl().length() > 255) {
            throw new PaymentException(PARAM_ERROR_CODE,"推送地址长度不超过255");
        }

        if (StringUtils.isBlank(request.getSignInfo())) {
            throw new PaymentException(PARAM_ERROR_CODE,"签名不能为空");
        }
    }

    public void checkAccount(PaymentRequest request) throws PaymentException{
        // 1.账户号是否有效
        if(!NumberUtils.isDigits(request.getAccount())){
            throw new PaymentException(PARAM_ERROR_CODE,"账户号无效");
        }

        Merchant merchant = merchantServiceImpl.queryMerchant(Integer.valueOf(request.getAccount()));
        if(merchant == null){
            throw new PaymentException(PARAM_ERROR_CODE,"账户号无效");
        }
        // 2.验签
        if(StringUtils.isBlank(merchant.getMerchantSignKey())){
            throw new PaymentException(PARAM_ERROR_CODE,"账户号无效");
        }
        String secureInfo = DigestUtils.sha256Hex(request.getAccount()
                + request.getOrderNo()
                + request.getOrderAmount()
                + request.getOrderCurrency()
                + request.getBillingFirstName()
                + request.getBillingLastName()
                + merchant.getMerchantSignKey());
        if(!request.getSignInfo().equalsIgnoreCase(secureInfo)){
            throw new PaymentException(PARAM_ERROR_CODE,"验签失败");
        }
    }

}
