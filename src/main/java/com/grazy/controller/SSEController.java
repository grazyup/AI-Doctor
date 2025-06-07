package com.grazy.controller;

import com.grazy.enums.SSEMsgType;
import com.grazy.utils.SSEServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: grazy
 * @Date: 2025-06-07 13:12
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("sse")
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
     * 向客户端推送单条消息
     * @param connectId
     * @param msg
     * @return
     */
    @GetMapping("/sentMessage")
    public SseEmitter sseConnect(@RequestParam String connectId, @RequestParam String msg){
        return SSEServer.sentMessage(connectId, msg, SSEMsgType.MESSAGE);
    }

}
