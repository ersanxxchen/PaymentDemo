package com.example.demo.framwork.exception;

import java.text.MessageFormat;

public class PaymentException extends RuntimeException {
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    private void setErrorCode(String code) {
        this.errorCode = code;
    }

    public PaymentException(String code,String message) {
        super(message);
        setErrorCode(code);
    }

    public PaymentException(String code,String message,String...info) {
        super(MessageFormat.format(message, (Object) info));
        setErrorCode(code);
    }

    public PaymentException(PaymentExceptionCode code, String info ){
        super(MessageFormat.format(code.getErrorMessage(),info));
        setErrorCode(code.toString());
    }

    public enum PaymentExceptionCode {

        PE200001("商户账户号{0}格式异常");

        private final String errorMessage;

        private PaymentExceptionCode(String value) {
            this.errorMessage = value;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
