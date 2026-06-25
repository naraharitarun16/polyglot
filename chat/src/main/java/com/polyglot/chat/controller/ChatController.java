package com.polyglot.chat.controller;

import com.polyglot.chat.model.ChatMessage;
import com.polyglot.chat.service.ChatMessageService;
import com.polyglot.chat.service.TranslationService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ChatController {

    private final TranslationService translationService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public ChatController(TranslationService translationService, ChatMessageService chatMessageService, SimpMessagingTemplate messagingTemplate) {
        this.translationService = translationService;
        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    public void sendMessage(ChatMessage message) {
        // If translation is needed and target language is different from source, process asynchronously
        String sourceLanguage = message.getLanguage() != null ? message.getLanguage() : "en";
        
        if (message.getTargetLanguage() != null && 
            !message.getTargetLanguage().isEmpty() && 
            !message.getTargetLanguage().equals(sourceLanguage)) {
            
            CompletableFuture.runAsync(() -> {
                try {
                    String translated = translationService.translate(
                        message.getContent(),
                        sourceLanguage,
                        message.getTargetLanguage()
                    ).block();

                    if (translated != null && !translated.isEmpty()) {
                        message.setTranslatedContent(translated);
                    }
                } catch (Exception e) {
                    System.err.println("Translation error: " + e.getMessage());
                    // Don't set translated content if translation fails
                }
                // Save message to database
                chatMessageService.saveMessage(message);
                // Send message after translation attempt (success or failure)
                messagingTemplate.convertAndSend("/topic/messages", message);
            }, executorService);
        } else {
            // Save message to database
            chatMessageService.saveMessage(message);
            // Send message immediately if no translation needed
            messagingTemplate.convertAndSend("/topic/messages", message);
        }
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public String handleTyping(String username) {
        return username + " is typing...";
    }
}

@RestController
@RequestMapping("/api/messages")
class ChatMessageRestController {
    
    private final ChatMessageService chatMessageService;

    public ChatMessageRestController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return chatMessageService.getAllMessages();
    }

    @GetMapping("/sender/{sender}")
    public List<ChatMessage> getMessagesBySender(@PathVariable String sender) {
        return chatMessageService.getMessagesBySender(sender);
    }
}

