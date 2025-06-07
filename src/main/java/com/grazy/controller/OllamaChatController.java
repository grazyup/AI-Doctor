package com.grazy.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: grazy
 * @Date: 2025-06-02 22:04
 * @Description:
 */

@RestController
public class OllamaChatController {

    @Resource
    private OllamaChatClient ollamaChatClient;

    /**
     *  同步返回结果
     * @param msg
     * @return
     */
    @GetMapping("/ollama/chat")
    public Object getOllamaChat(@RequestParam String msg){
        return ollamaChatClient.call(msg);
    }

    /**
     *  流式返回结果
     * @param msg
     * @return
     */
    @GetMapping("/ollama/stream")
    public Flux<ChatResponse> getOllamastream(@RequestParam String msg){
        return ollamaChatClient.stream(new Prompt(new UserMessage(msg)));
    }
}

