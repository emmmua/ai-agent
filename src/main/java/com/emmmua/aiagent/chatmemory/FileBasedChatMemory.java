package com.emmmua.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBasedChatMemory implements ChatMemory {

    /**
     * 存储对话文件的路径
     */
    private final String BADER_FILE_PATH;

    private static final Kryo kryo = new Kryo();

    static {
        // 不需要手动注册，改成false
        kryo.setRegistrationRequired(false);

        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }


    /**
     * 构造函数
     *
     * @param dir 对话文件路径
     */
    public FileBasedChatMemory(String dir) {
        this.BADER_FILE_PATH = dir;

        // 判断文件是否存在
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            // 创建目录
            baseDir.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, Message message) {
        // 获取往期消息
        List<Message> messageList = getOrCreateConversation(conversationId);
        // 添加新消息
        messageList.add(message);
        // 保存会话消息
        saveConversation(conversationId, messageList);
    }

    /**
     * 添加多条消息
     *
     * @param conversationId 会话ID
     * @param messages       消息列表
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
        // 获取往期消息
        List<Message> messageList = getOrCreateConversation(conversationId);
        // 添加新消息
        messageList.addAll(messages);
        // 保存会话消息
        saveConversation(conversationId, messageList);
    }

    /**
     * 获取最后 lastN 条消息
     *
     * @param conversationId 会话ID
     * @param lastN          最后 N 条消息
     * @return messages
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 获取往期消息
        List<Message> messageList = getOrCreateConversation(conversationId);
        return messageList.subList(Math.max(0, messageList.size() - lastN), messageList.size());
    }

    /**
     * 清空会话消息
     *
     * @param conversationId 会话ID
     */
    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取会话消息列表
     *
     * @param conversationId 会话ID
     * @return messages
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }


    /**
     * 保存会话消息列表
     *
     * @param conversationId 会话ID
     * @param messages       消息列表
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取对话文件
     *
     * @param conversationId 会话ID
     * @return File
     */
    private File getConversationFile(String conversationId) {
        return new File(BADER_FILE_PATH, conversationId + ".kryo");
    }
}
