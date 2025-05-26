package com.emmmua.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoveAppDocumentReaderTest {

    @Resource
    private LoveAppDocumentReader loveAppDocumentReader;

    @Test
    void loadMarkdowns() {
        loveAppDocumentReader.loadMarkdowns();
    }
}