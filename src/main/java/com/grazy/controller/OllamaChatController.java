package com.grazy.controller;

import com.grazy.entity.InquireInput;
import com.grazy.service.OllamaService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @Author: grazy
 * @Date: 2025-06-02 22:04
 * @Description: 测试调用本地 ollama 接口
 */

@RestController
public class OllamaChatController {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Resource
    private OllamaService ollamaService;

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


    /**
     * 最终接口-调用大模型解答疑难杂症
     * @param inquireInput
     */
    @PostMapping("/ai/inquire")
    public void inquireDoctor(@RequestBody InquireInput inquireInput){
        ollamaService.inquireDoctor(inquireInput.getCurrentUserName(), inquireInput.getMessage());
    }
}

