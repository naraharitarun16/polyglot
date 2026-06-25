package com.polyglot.chat.controller;

import com.polyglot.chat.model.ChatMessage;
import com.polyglot.chat.service.TranslationService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ChatController {

    private final TranslationService translationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public ChatController(TranslationService translationService, SimpMessagingTemplate messagingTemplate) {
        this.translationService = translationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    public void sendMessage(ChatMessage message) {
        // Send original message immediately
        messagingTemplate.convertAndSend("/topic/messages", message);

        // If translation is needed, process asynchronously
        if (message.getTargetLanguage() != null && !message.getTargetLanguage().isEmpty()) {
            CompletableFuture.runAsync(() -> {
                try {
                    String translated = translationService.translate(
                        message.getContent(),
                        message.getLanguage() != null ? message.getLanguage() : "en",
                        message.getTargetLanguage()
                    ).block();

                    message.setTranslatedContent(translated);
                    messagingTemplate.convertAndSend("/topic/messages", message);
                } catch (Exception e) {
                    // Handle translation error
                    message.setTranslatedContent("Translation failed");
                    messagingTemplate.convertAndSend("/topic/messages", message);
                }
            }, executorService);
        }
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public String handleTyping(String username) {
        return username + " is typing...";
    }
}
