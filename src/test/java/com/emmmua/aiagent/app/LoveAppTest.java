package com.emmmua.aiagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void chat() {
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


    @Test
    void chatWithReport() {
        String chatId = UUID.randomUUID().toString();

        String message = "你好，我是Fivk，我想让另一半（清清）更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.chatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void chatWithRag() {
        String chatId = UUID.randomUUID().toString();

        String message = "我已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.chatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void chatWithPgVector() {
        String chatId = UUID.randomUUID().toString();
        String message = "我正在学习，但是没有找到相关的资料，你能帮我找一下吗？";
        String answer = loveApp.chatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}