package com.example.demo.framwork.exception;

import com.example.demo.project.domain.PaymentResponse;
import com.example.demo.project.service.impl.BaseParamCheckServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@ControllerAdvice
public class PaymentExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = PaymentException.class)
    @ResponseBody
    public PaymentResponse bizExceptionHandler(HttpServletRequest req, PaymentException e){
        logger.error("发生业务异常！code:{};message:{}",e.getErrorCode(),e.getMessage());
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setResponseCode(e.getErrorCode());
        paymentResponse.addInfo("message",e.getMessage());
        return paymentResponse;
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public PaymentResponse exceptionHandler(HttpServletRequest req, Exception e){
        logger.error("未知异常！原因是:",e);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setResponseCode("5000000");
        HashMap<String, String> body = new HashMap<>();
        body.put("message","System Error");
        paymentResponse.addDto(body);
        return paymentResponse;
    }
}
