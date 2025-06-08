package com.grazy.controller;

import com.grazy.entity.ChatRecordDo;
import com.grazy.service.ChatRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: grazy
 * @Date: 2025-06-08 23:47
 * @Description: 通话记录接口
 */

@RestController
public class ChatRecordController {

    @Resource
    private ChatRecordService chatRecordService;

    @GetMapping("ai/chatRecord")
    public List<ChatRecordDo> fetchChatRecord(@RequestParam String who){
        return chatRecordService.fetchChatRecord(who);
    }

}
