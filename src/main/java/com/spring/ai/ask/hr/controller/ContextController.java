package com.spring.ai.ask.hr.controller;

import com.spring.ai.ask.hr.exception.InvalidPromptException;
import com.spring.ai.ask.hr.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ask-hr")
public class ContextController {

    @Autowired
    private ContextService contextService;

    @GetMapping
    public String testApi(@RequestParam String question) throws InvalidPromptException {
        return contextService.testApi(question);
    }

    @PostMapping(value = "/pdf")
    public void handlePdfUpload(@RequestParam("file") List<MultipartFile> file) throws IOException {
        contextService.handlePdfUpload(file);
    }

}
