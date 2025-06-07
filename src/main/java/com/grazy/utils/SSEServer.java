package com.grazy.utils;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: grazy
 * @Date: 2025-06-05 22:06
 * @Description: SSE 服务工具类
 */

@Slf4j
public class SSEServer {

    // 保存连接信息
    private static Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();

    public static SseEmitter connect(String connectId){
        // 默认过期时间为30s,目前设置永不不过期
        SseEmitter sseEmitter = new SseEmitter(0L);

        // 任务完成回调
        sseEmitter.onCompletion(() ->{
            SSECompletion(connectId);
        });

        // 任务超时回调
        sseEmitter.onTimeout(() ->{
            SSETimeout(connectId);
        });

        // 任务失败回调 (Throwable 是 Exception 和 Error 的父类)
        sseEmitter.onError(throwable ->{
            SSEError(connectId, throwable);
        });

        // 储存
        sseClients.put(connectId, sseEmitter);
        log.info("对象{}，创建SSE连接成功", connectId);
        return sseEmitter;
    }


    private static void SSECompletion(String connectId){
        log.info("客户端{}，SSE任务执行完成", connectId);
        removeSSE(connectId);
    }

    private static void SSEError(String connectId, Throwable throwable){
        log.info("客户端{}，SSE连接发生错误，错误信息：{}", connectId, throwable.getMessage());
        removeSSE(connectId);
    }

    private static void SSETimeout(String connectId){
        log.info("客户端{}, SSE连接超时", connectId);
        removeSSE(connectId);
    }

    private static void removeSSE(String connectId) {
        if (StringUtils.isBlank(connectId)) {
            log.error("移除SSE-传入的连接ID为空");
        }
        sseClients.remove(connectId);
        log.info("{}连接移除成功", connectId);
    }
}
