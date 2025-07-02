package com.emmmua.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class PgVectorVectorStoreTest {

    /**
     * 测试向量数据库
     * 如果这里出现报错，可能是因为配置了Ollama和阿里的DashScope，所以有两个EmbeddingModel的Bean，导致报错
     * 解决方法：删除spring-ai-ollama-spring-boot-starter依赖文件，或者案例的依赖
     */
    @Resource
    VectorStore vectorStore;

    @Test
    void test() {
        List<Document> documents = List.of(
                new Document("人工智能正在迅速发展，它正在改变我们的生活和工作方式。", Map.of("meta1", "科技")),
                new Document("机器学习是AI的一个重要分支，它使计算机能够从数据中学习并做出决策。"),
                new Document("深度学习技术推动了图像识别和自然语言处理领域的突破性进展。", Map.of("meta2", "AI技术")));

        // 添加文档
        vectorStore.add(documents);

        // 相似度查询
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query("学习").topK(2).build());
        Assertions.assertNotNull(results);
    }
}
