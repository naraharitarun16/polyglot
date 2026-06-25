package com.polyglot.chat.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private String language;
    private String translatedContent;
    private String targetLanguage;
}
