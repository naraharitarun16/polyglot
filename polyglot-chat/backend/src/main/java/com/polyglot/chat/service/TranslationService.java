package com.polyglot.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TranslationService {

    private final WebClient webClient;

    @Value("${huggingface.api.key}")
    private String apiKey;

    public TranslationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api-inference.huggingface.co").build();
    }

    public Mono<String> translate(String text, String sourceLang, String targetLang) {
        String model = "facebook/nllb-200-distilled-600M"; // Meta NLLB-200 model

        Map<String, Object> requestBody = Map.of(
            "inputs", text,
            "parameters", Map.of(
                "src_lang", sourceLang,
                "tgt_lang", targetLang
            )
        );

        return webClient.post()
            .uri("/models/" + model)
            .header("Authorization", "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseTranslationResponse);
    }

    private String parseTranslationResponse(String response) {
        // Simple parsing for Hugging Face response
        // In a real implementation, parse JSON properly
        return response; // Placeholder
    }
}
