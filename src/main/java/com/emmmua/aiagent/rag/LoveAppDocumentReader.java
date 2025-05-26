package com.emmmua.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 恋爱大师应用RAG文档Reader
 */
@Component
@Slf4j
public class LoveAppDocumentReader {


    /**
     * 相比官方案例中的Resource，ResourcePatternResolver可以读取多篇文档
     */
    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentReader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }


    /**
     * 读取多篇Markdown文档
     *
     * @return List<Document>
     */
    public List<Document> loadMarkdowns() {
        List<Document> allDocuments = new ArrayList<>();

        // 加载多篇MarkDown文档
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            // 这里Resource和官方文档中使用的方法是一样的
            for (Resource resource : resources) {
                String filename = resource.getFilename();

                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        // 允许用水平线（如---）分隔生成多个子文档
                        .withHorizontalRuleCreateDocument(true)
                        // 是否忽略代码块内容
                        .withIncludeCodeBlock(false)
                        // 是否忽略块引用
                        .withIncludeBlockquote(false)
                        // 添加额外元数据，设置文件名为真实的filename（用于标识当前处理的文件）。
                        .withAdditionalMetadata("filename", filename)
                        .build();

                log.info("正在处理文件：{}", filename);
                // 创建MarkdownDocumentReader
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                // 读取：你可能认为每次循环只处理一篇 Markdown "文件"，但这个文件可能会被拆分成多个 Document，所以要用 addAll(reader.get()) 来添加所有生成的文档片段。
                allDocuments.addAll(reader.get());
            }

        } catch (IOException e) {
            log.error("MarkDown 文档加载失败", e);
        }

        return allDocuments;
    }
}
