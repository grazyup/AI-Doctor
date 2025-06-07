package com.grazy.enums;

/**
 * @Author: grazy
 * @Date: 2025-06-07 14:07
 * @Description: sse 消息推送类型
 */
public enum SSEMsgType {

    MESSAGE("message", "单次推送普通消息"),
    ADD("add", "追加的方式,用于流式 Stream 推送"),
    FINISH("finish", "消息完成"),
    DONE("done", "消息完成");

    public final String type;

    public final String desc;

    SSEMsgType(String type, String desc){
        this.type = type;
        this.desc = desc;
    }


}
