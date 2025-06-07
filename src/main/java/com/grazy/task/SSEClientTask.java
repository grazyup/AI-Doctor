package com.grazy.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.grazy.utils.SSEServer.sseClients;

/**
 * @Author: grazy
 * @Date: 2025-06-07 13:30
 * @Description:
 */
@Component
@Slf4j
public class SSEClientTask {

    /**
     * 定时(5分钟)打印连接资源数据
     */
    @Scheduled(fixedRate = 300000)
    private void printfSSEClientsData(){
        Set<String> connectIds = sseClients.keySet();
        log.info("sse 连接详情数据：{}， 总连接数为：{}", connectIds, connectIds.size());
    }
}
