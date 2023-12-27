package com.example.demo.project.schedule;

import com.example.demo.common.util.DateUtils;
import com.example.demo.project.controller.PaymentController;
import com.example.demo.project.domain.DO.Channel;
import com.example.demo.project.domain.DO.NormalTransaction;
import com.example.demo.project.service.impl.MerchantServiceImpl;
import com.example.demo.project.service.impl.PaymentServiceImpl;
import com.example.demo.project.service.impl.TransactionServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration  //标记配置类
@EnableScheduling   //开启定时任务
public class ScheduleJob {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJob.class);


    @Autowired
    private PaymentServiceImpl paymentServiceImpl;
    @Autowired
    private TransactionServiceImpl transactionServiceImpl;
    @Autowired
    private MerchantServiceImpl merchantServiceImpl;

    //保存交易推送
//    @Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.MINUTES)
    private void saveTransactionNotice() {
        logger.info("交易推送任务开始:{}", LocalDateTime.now());
        try {
            List<NormalTransaction> transactionList = transactionServiceImpl.getAllUnNoticeTransaction();
            Map<Integer, Channel> channelMap = new HashMap<>();
            for (NormalTransaction transaction : transactionList) {
                if (channelMap.get(transaction.getChannelId()) == null) {
                    Channel channel = merchantServiceImpl.queryChannel(transaction.getChannelId());
                    channelMap.put(channel.getChannelId(), channel);
                }
                Channel transChannel = channelMap.get(transaction.getChannelId());
                String now = DateUtils.getDateAgo(0, 0, 0);
                if (StringUtils.equals("Cash App", transChannel.getBank())
                        && StringUtils.isBlank(transaction.getNoticeDate())) {
                    transaction.setNoticeStatus(0);
                    transaction.setNoticeDate(DateUtils.getDateAgo(0, -5, 0));
                } else if (StringUtils.equals("Cash App", transChannel.getBank())
                        && transaction.getNoticeDate().compareTo(now) > 0) {
                    continue;
                } else {
                    paymentServiceImpl.noticeMerchant(transaction);
                    transaction.setNoticeStatus(1);
                    transaction.setNoticeDate(now);
                }
                transactionServiceImpl.updateNoticeTransaction(transaction);
            }
        } catch (Exception e) {
            logger.error("交易推送任务异常", e);
        }
        logger.info("交易推送任务结束:{}", LocalDateTime.now());
    }

}
