package com.preeti.sansarcart.controller;

import com.preeti.sansarcart.service.sentimental.analysis.SentimentAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentimentController {

    private final SentimentAnalysisService sentimentService;

    public SentimentController(SentimentAnalysisService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @GetMapping("/sentiment")
    public String getSentiment(@RequestParam String text) {
        return sentimentService.analyzeSentiment(text);
    }
}
