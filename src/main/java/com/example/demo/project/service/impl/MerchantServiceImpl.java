package com.example.demo.project.service.impl;

import com.example.demo.project.controller.PaymentController;
import com.example.demo.project.domain.DO.Channel;
import com.example.demo.project.domain.DO.Merchant;
import com.example.demo.project.mapper.ChannelMapper;
import com.example.demo.project.mapper.MerchantMapper;
import com.example.demo.project.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private ChannelMapper channelMapper;

    public Merchant queryMerchant(Integer merchantId) {
        Merchant merchant = merchantMapper.queryMerchant(merchantId);
        if (merchant != null && merchant.getChannelId() != null) {
            Channel channel = channelMapper.queryChannel(merchant.getChannelId());
            merchant.setChannel(channel);
        }
        return merchant;
    }
}
