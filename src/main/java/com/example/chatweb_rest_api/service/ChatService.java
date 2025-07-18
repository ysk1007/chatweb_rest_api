package com.example.chatweb_rest_api.service;

import org.springframework.stereotype.Service;

import com.example.chatweb_rest_api.dto.ChatHistory;
import com.example.chatweb_rest_api.repository.ChatHistoryRepository;

@Service
public class ChatService {
	
	private final ChatHistoryRepository chatHistoryRepository;
	
	public ChatService(ChatHistoryRepository chatHistoryRepository) {
		this.chatHistoryRepository = chatHistoryRepository;
	}
	
	// 채팅 내용 저장
	public ChatHistory addChatHistory(ChatHistory chatHistory) {
		return chatHistoryRepository.save(chatHistory);
	}
}
