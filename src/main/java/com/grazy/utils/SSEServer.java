package com.grazy.utils;

import com.grazy.enums.SSEMsgType;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: grazy
 * @Date: 2025-06-05 22:06
 * @Description: SSE 服务工具类
 */

@Slf4j
public class SSEServer {

    // 保存连接信息
    public static Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();

    /**
     * 建立 sse 连接
     * @param connectId
     * @return
     *  todo:
     *      问题： 这里会有一个重连的问题，就是在执行 complete 之后，会移除这个 map 中维护的连接对象，那么客户端在断开后会发起重连机制，
     *          就会重新使用相同的 connectId 不同的 sseEmitter 对象存储到 map 集合中，那么在调用统计数据的时候前后不就没有发生改变吗 ？？？
     *      解决方案:
     *          1. 客户端禁止重连
     *          2. 前端是否可以判断是手动断开还是因为其他问题断开的？？
     */
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
        log.info("对象【{}】，创建SSE连接成功", connectId);
        return sseEmitter;
    }


    /**
     * 向客户端推动消息
     * @param connectId
     * @param msg
     * @return
     */
    public static void sentMessage(String connectId, String msg, SSEMsgType sseMsgType) {
        // 判断是或存在当前连接对象
        SseEmitter sseEmitter = sseClients.get(connectId);
        if(sseEmitter == null){
            log.info("连接对象【{}】不存在", connectId);
            return;
        }
        try {
            SseEmitter.SseEventBuilder message = SseEmitter.event()
                    .id(connectId)
                    .name(sseMsgType.type)
                    .data(msg);
            sseEmitter.send(message);
        } catch (IOException e) {
            log.error("对象【{}】推送消息发生异常, 异常信息：{}", connectId, e.getMessage());
            removeSSE(connectId);
        }
    }


    /**
     * 广播消息到全部在线的客户端
     * @param msg
     * @param sseMsgType
     */
    public static void fanoutSentMessage(String msg, SSEMsgType sseMsgType){
        Set<String> connectIds = sseClients.keySet();
        connectIds.forEach(connectId -> {
           sentMessage(connectId, msg, sseMsgType);
        });
    }


    /**
     * 完成任务/会话关闭，断开连接
     * @param connectId
     */
    public static void stopSSEConnect(String connectId) {
        SseEmitter sseEmitter = sseClients.get(connectId);
        if(sseEmitter != null){
            // 这个里面会回调 onComplete方法 (这个回调是异步的，如果在回调执行前就查询在线数量，有可能会查到没有移除后的数量)
            sseEmitter.complete();
            // 为了保证移除的准确性，可以先执行同步移除
            removeSSE(connectId);
            log.info("会话关闭成功");
        } else {
            log.warn("会话已关闭");
        }
    }

    /****************************** private **************************************/

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
