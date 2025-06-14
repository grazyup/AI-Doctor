package com.grazy.service;

import org.springframework.stereotype.Service;

/**
 * @Author: grazy
 * @Date: 2025-06-08 21:11
 * @Description:
 */
public interface OllamaService {

    /**
     * 询问大模型医生
     * @param userName
     * @param message
     */
    public void inquireDoctor(String userName, String message);

}
