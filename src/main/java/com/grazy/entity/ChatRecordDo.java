package com.grazy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: grazy
 * @Date: 2025-06-08 23:39
 * @Description:
 */
@Data
@TableName("chat_record")
public class ChatRecordDo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 会话聊天内容 用户/AI
     */
    private String content;

    /**
     * 会话类型
     */
    private String chatType;

    /**
     * 消息创建时间
     */
    private LocalDateTime chatTime;

    /**
     * 消息归属人
     */
    private String member;

}
