package com.grazy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.grazy.entity.ChatRecordDo;
import com.grazy.enums.ChatObjectTypeEnum;
import com.grazy.mapper.ChatRecordMapper;
import com.grazy.service.ChatRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: grazy
 * @Date: 2025-06-09 0:44
 * @Description:
 */

@Service
@Slf4j
public class ChatRecordServiceImpl implements ChatRecordService {

    @Resource
    private ChatRecordMapper chatRecordMapping;

    @Override
    public void addChatRecord(String userName, String content, ChatObjectTypeEnum objectTypeEnum) {
        log.info("开始持久化聊天消息");
        ChatRecordDo chatRecordDo = new ChatRecordDo();
        chatRecordDo.setMember(userName);
        chatRecordDo.setContent(content);
        chatRecordDo.setChatType(objectTypeEnum.type);
        chatRecordDo.setChatTime(LocalDateTime.now());

        // 持久化消息
        chatRecordMapping.insert(chatRecordDo);

        log.info("消息归属：【{}】, 消息内容：{}， 发消息者的身份：{}", userName, content, objectTypeEnum.desc);
    }

    @Override
    public List<ChatRecordDo> fetchChatRecord(String who) {
        LambdaQueryWrapper<ChatRecordDo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ChatRecordDo::getMember, who);
        return chatRecordMapping.selectList(lambdaQueryWrapper);
    }
}
