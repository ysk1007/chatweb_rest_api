package com.example.chatweb_rest_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.chatweb_rest_api.dto.ChatHistory;
import com.example.chatweb_rest_api.dto.ChatHistoryMapping;
import com.example.chatweb_rest_api.dto.LoginHistoryMapping;
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
	
	// 대화 이력 조회
	public Page<ChatHistoryMapping> getChatHistoryList(Long userNo, int currentPage){
		int pageSize = 5;
	    int pageNumber = currentPage - 1;

	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("chatNo").descending());
	    Page<ChatHistoryMapping> historyList = chatHistoryRepository.findByUser_UserNo(userNo, pageable);

	    return historyList;
	}
}
