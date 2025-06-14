package com.grazy.controller;

import com.grazy.enums.SSEMsgType;
import com.grazy.utils.SSEServer;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: grazy
 * @Date: 2025-06-07 13:12
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping(value = "sse")
public class SSEController {

    /**
     * sse 建立连接
     * @param connectId
     * @return
     */
    @GetMapping(path = "connect", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter sseConnect(@RequestParam String connectId){
        return SSEServer.connect(connectId);
    }


    /**
     * 向单一客户端推送单条消息 (postman调用测试)
     * @param connectId
     * @param msg
     * @return
     */
    @GetMapping("/sentMessage")
    public Object sseConnect(@RequestParam String connectId, @RequestParam String msg){
        SSEServer.sentMessage(connectId, msg, SSEMsgType.MESSAGE);
        return "ok";
    }


    /**
     * 广播推送单条消息 (postman调用测试)
     * @param msg
     * @return
     */
    @GetMapping("/fanoutSentMessage")
    public Object fanoutSentMessage(@RequestParam String msg){
        SSEServer.fanoutSentMessage(msg, SSEMsgType.MESSAGE);
        return "ok";
    }


    /**
     * 使用追加的方式推动消息 (postman调用测试)
     * @param msg
     * @return
     */
    @GetMapping("/addSentMessage")
    public Object addSentMessage(@RequestParam String msg,
                                 @RequestParam(required = false) String connectId) throws Exception{

        char[] charArray = msg.toCharArray();
        for (char c : charArray) {
            Thread.sleep(100);
            String msgStr = String.valueOf(c);
            if (StringUtils.isBlank(connectId)) {
                // 使用广播的方式
                SSEServer.fanoutSentMessage(msgStr, SSEMsgType.ADD);
            } else {
                // 向指定用户推送
                SSEServer.sentMessage(connectId, msgStr, SSEMsgType.ADD);
            }
        }
        return "ok";
    }


    /**
     * 断开 sse 连接
     *   调用这个接口断开连接后，sse客户端会触发重连机制，再次携带相同的connectId进行连接
     * @param connectId
     * @return
     */
    @GetMapping("/stopSSEConnect")
    public Object stopSSEConnect(@RequestParam String connectId){
        SSEServer.stopSSEConnect(connectId);
        return "ok";
    }
}
