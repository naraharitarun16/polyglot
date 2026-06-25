package com.polyglot.chat.service;

import com.polyglot.chat.model.ChatMessage;
import com.polyglot.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public Optional<ChatMessage> getMessageById(Long id) {
        return chatMessageRepository.findById(id);
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ChatMessage> getMessagesBySender(String sender) {
        return chatMessageRepository.findBySenderOrderByCreatedAtDesc(sender);
    }

    public void deleteMessage(Long id) {
        chatMessageRepository.deleteById(id);
    }

    public long getTotalMessageCount() {
        return chatMessageRepository.count();
    }
}
