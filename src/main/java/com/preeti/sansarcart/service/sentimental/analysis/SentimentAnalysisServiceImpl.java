package com.preeti.sansarcart.service.sentimental.analysis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Primary
public class SentimentAnalysisServiceImpl implements SentimentAnalysisService {

    @Value("${google.api.key}")
    private String apiKey;

    private final String model = "gemini-2.5-flash-preview-04-17";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String analyzeSentiment(String inputText) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", "Analyze the sentiment of the following text and return result in JSON with key sentiment: positive, negative, or neutral. Text: " + inputText)
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // Extract the clean JSON from the response
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response.getBody(), Map.class);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                String text = (String) parts.get(0).get("text");

                // Extract JSON content from markdown code block if present
                if (text.startsWith("```json")) {
                    text = text.replaceAll("```json\\n", "")
                            .replaceAll("\\n```", "")
                            .trim();
                }

                return text;
            }

            return "{\"sentiment\": \"unknown\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"sentiment\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}