package com.example.demo.project.service;

import com.example.demo.framwork.exception.PaymentException;
import com.example.demo.project.domain.PaymentRequest;
import org.apache.poi.ss.formula.functions.T;

import java.util.Objects;

public interface ParamCheckService {

    public void checkParam(PaymentRequest request) throws PaymentException;
}
