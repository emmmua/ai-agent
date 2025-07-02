package com.emmmua.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
//@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentReader loveAppDocumentReader;

    /**
     * 初始化基于内存的向量数据库
     *
     * @param dashscopeEmbeddingModel 这是阿里云的向量模型
     * @return VectorStore
     */
    @Bean
    public VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        // 创建基于内存的向量模型，使用阿里云的向量模型
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();

        // 加载数据
        List<Document> documents = loveAppDocumentReader.loadMarkdowns();

        // 添加数据
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
