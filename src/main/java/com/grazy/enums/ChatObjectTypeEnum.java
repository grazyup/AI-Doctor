package com.grazy.enums;

import lombok.Data;

/**
 * @Author: grazy
 * @Date: 2025-06-09 0:46
 * @Description: 聊天对象枚举
 */
public enum ChatObjectTypeEnum {

    USER("user", "用户"),
    BOT("bot","AI大模型");

    public final String type;

    public final String desc;

    ChatObjectTypeEnum(String type, String desc){
        this.desc = desc;
        this.type = type;
    }

}
