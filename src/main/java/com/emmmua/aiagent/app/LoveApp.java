package com.emmmua.aiagent.app;

import com.emmmua.aiagent.advisor.MyLoggerAdvisor;
import com.emmmua.aiagent.chatmemory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";


    /**
     * 初始化chatClient
     *
     * @param dashscopeChatModel 模型（dashscopeChatModel是阿里的模型，为我们配置文件用的是dashscope，所以这里注入就能使用）
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 初始化一个基于内存的对话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();

        // 初始化一个基于文件对话记忆
        String filePath = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(filePath);

        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义的日志记录，可按需开启
                        new MyLoggerAdvisor()
                        // 自定义Re2，可按需开启
//                        new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话（支持多轮对话记忆）
     *
     * @param question 聊天问题
     * @param chatId   会话ID
     * @return 回复文本
     */
    public String chat(String question, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(question)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();

        String text = chatResponse.getResult().getOutput().getText();
        log.info("text: {}", text);
        return text;
    }

    /**
     * 恋爱报告结构体
     *
     * @param title
     * @param suggestions 建议列表
     */
    record LoveReport(String title, List<String> suggestions) {

    }

    /**
     * AI 恋爱报告功能（实战结构化输出）
     *
     * @param question 聊天问题
     * @param chatId   会话ID
     * @return 回复文本
     */
    public LoveReport chatWithReport(String question, String chatId) {
        LoveReport loveReport = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(question)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        return loveReport;
    }
}
