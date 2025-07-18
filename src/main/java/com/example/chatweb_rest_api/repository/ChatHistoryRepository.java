package com.example.chatweb_rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatweb_rest_api.dto.ChatHistory;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long>{

}
