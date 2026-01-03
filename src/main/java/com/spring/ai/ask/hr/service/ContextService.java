package com.spring.ai.ask.hr.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContextService {
    private ChatClient mistralChatClient;

    private ChatMemory chatMemory;

    private final Map<String, String> pdfTextContents = new LinkedHashMap<>();

    @Value("${file.upload.path}")
    private String filePath;

    @Autowired
    private VectorStore vectorStore;

    public ContextService(MistralAiChatModel chatModel){
        this.chatMemory = MessageWindowChatMemory.builder().build();
        this.mistralChatClient = ChatClient.builder(chatModel)
                .build();
    }

    public String testApi(String question){
        return mistralChatClient.prompt(question).call().content();
    }

    public void initializePdfProcessing(String filePath) throws IOException {
        createDocumentsFromPdf(filePath);
        System.out.println(pdfTextContents);
    }

    private void createDocumentsFromPdf(String filePath) throws IOException {
        List<Document> documents = new ArrayList<>();
        ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(filePath, PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageBottomMargin(0)
                .withPagesPerDocument(1)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .build())
                .build());

        vectorStore.add(documents);
    }

    public void handlePdfUpload() throws MalformedURLException {
        try {
            initializePdfProcessing(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
