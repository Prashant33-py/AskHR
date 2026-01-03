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
        PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(filePath));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        for (int i = 1; i <= document.getNumberOfPages(); i++) {
            pdfStripper.setStartPage(i);
            pdfStripper.setEndPage(i);
            String pdfText = pdfStripper.getText(document);
            String cleanedText = pdfText.replaceAll("\\s+", " ").trim();
            pdfTextContents.put("page_" + i, cleanedText);
        }
        document.close();
        createDocuments();
        System.out.println(pdfTextContents);
    }

    private void createDocuments() {
        List<Document> documents = new ArrayList<>();
        for (String pageKey : pdfTextContents.keySet()) {
            Document doc = Document.builder()
                    .id(pageKey)
                    .text(pdfTextContents.get(pageKey))
                    .metadata("source", filePath)
                    .metadata("page", pageKey)
                    .build();
        }
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
