package com.emmmua.aiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Spring AI 框架调用 AI 大模型
 * 实现CommandLineRunner是为了启动时执行
 */
//@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    /**
     * Resource是根据名称注入，ChatModel是一个接口。使用阿里灵积模型，
     * 名称必须是dashscopeChatModel，不然找不到对应名字的Bean。
     */
    @Resource
    private ChatModel dashscopeChatModel;


    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好，我是Fivk"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}
