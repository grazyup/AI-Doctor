package com.grazy.entity;

import lombok.Data;

/**
 * @Author: grazy
 * @Date: 2025-06-08 21:08
 * @Description: 询问交互参数类
 */

@Data
public class InquireInput {

    /**
     * 实际上就是与 connectId 一样
     */
    private String userName;

    /**
     * 询问的消息
     */
    private String message;
}
