package com.grazy.service;

import com.grazy.enums.ChatObjectTypeEnum;

/**
 * @Author: grazy
 * @Date: 2025-06-09 0:44
 * @Description: 聊天记录服务层
 */
public interface ChatRecordService {

    /**
     * 持久化聊天记录
     */
    public void addChatRecord(String userName, String content, ChatObjectTypeEnum objectTypeEnum);
}
