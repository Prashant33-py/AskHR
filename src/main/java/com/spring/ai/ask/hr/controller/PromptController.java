package com.spring.ai.ask.hr.controller;

import com.spring.ai.ask.hr.service.ContextService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/ask-hr/prompt")
public class PromptController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ContextService contextService;

    @PostMapping
    public ResponseEntity<List<Document>> getSimilarContent(@RequestParam String requestMessage){
        return ResponseEntity.ok().body(vectorStore.similaritySearch(requestMessage));
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestParam String question){
        return contextService.askQuestion(question);
    }

}
