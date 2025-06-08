package com.grazy.service.impl;

import com.grazy.enums.ChatObjectTypeEnum;
import com.grazy.enums.SSEMsgType;
import com.grazy.service.ChatRecordService;
import com.grazy.service.OllamaService;
import com.grazy.utils.SSEServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

/**
 * @Author: grazy
 * @Date: 2025-06-08 21:15
 * @Description:
 */
@Slf4j
@Service
public class OllamaServiceImpl implements OllamaService {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Resource
    private ChatRecordService chatRecordService;

    @Override
    public void inquireDoctor(String userName, String message) {
        log.info("开始调用医生大模型处理疑难杂症~~~");
        log.info("用户：{}，询问症状：{}", userName, message);
        // 持久化用户提问的会话消息
        chatRecordService.addChatRecord(userName, message, ChatObjectTypeEnum.USER);

        // 调用大模型
        Flux<ChatResponse> streamResponse = ollamaChatClient.stream(new Prompt(new UserMessage(message)));
        Stream<String> contentList = streamResponse.toStream().map(chatResponse -> {
            // 解析出大模型返回的一段段消息
            String content = chatResponse.getResult().getOutput().getContent();
            // 采用流式的方式回显消息
            SSEServer.sentMessage(userName, content, SSEMsgType.ADD);
            log.info("用户：【{}】，大模型返回的信息片段为：{}", userName, content);
            return content;
        });

        // 将流式数据拼接成一段字符串
        StringBuilder result = new StringBuilder();
        contentList.forEach(result::append);

        // 持久化AI回答的会话消息
        chatRecordService.addChatRecord(userName, result.toString(), ChatObjectTypeEnum.BOT);

        // 最终结束还要返回一个结束标识
        SSEServer.sentMessage(userName, "sent-over", SSEMsgType.FINISH);
        log.info("用户：【{}】, 大模型响应的消息推送完成", userName);
    }
}
