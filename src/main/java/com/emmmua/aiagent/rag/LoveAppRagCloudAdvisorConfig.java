package com.emmmua.aiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义基于阿里云知识库服务的RAG顾问配置
 */
@Configuration
@Slf4j
public class LoveAppRagCloudAdvisorConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;


    /**
     * 创建一个恋爱知识问答的顾问
     * 该方法初始化了一个DashScope API对象，并使用该对象连同一个指定的索引名
     * 创建了一个文档检索器，最后构建并返回一个带有该检索器的检索增强顾问
     *
     * @return Advisor 一个恋爱知识问答的顾问实例
     */
    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        // 初始化DashScope API对象
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);

        // 定义索引名，用于后续的文档检索
        final String indexName = "恋爱知识问答";

        // 创建一个DashScope文档检索器，使用之前定义的索引名
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(indexName)
                        .build());

        // 构建并返回一个检索增强顾问，包含之前创建的文档检索器
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }

}
