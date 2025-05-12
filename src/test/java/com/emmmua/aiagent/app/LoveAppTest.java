package com.emmmua.aiagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();

        // 第一轮
        String question = "你好，我是Fivk";
        String answer = loveApp.chat(question, chatId);

        // 第二轮
        question = "我想让我的另一半(清清)更爱我";
        answer = loveApp.chat(question, chatId);

        // 第三轮
        question = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.chat(question, chatId);
    }
}