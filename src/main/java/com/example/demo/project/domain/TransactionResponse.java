package com.example.demo.project.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TransactionResponse {

    private String responseCode;

    private Map<String, Object> body;

    public TransactionResponse() {
        this.setResponseCode("SUC0000");
        this.setBody(new HashMap<>());
    }
    public void addDto(Object o) {
        Map<String, Object> dtoMap = this.getBody();
        dtoMap.put(o.getClass().getSimpleName(), o);
        this.setBody(dtoMap);
    }
    public void addInfo(String name,String value) {
        Map<String, Object> dtoMap = this.getBody();
        dtoMap.put(name, value);
        this.setBody(dtoMap);
    }
}
