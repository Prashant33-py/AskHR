package com.spring.ai.ask.hr.controller;

import com.spring.ai.ask.hr.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/ask-hr")
public class ContextController {

    @Autowired
    private ContextService contextService;

    @GetMapping
    public String testApi(@RequestParam String question){
        return contextService.testApi(question);
    }

    @PostMapping(value = "/pdf")
    public void handlePdfUpload() {
        try {
            contextService.handlePdfUpload();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
