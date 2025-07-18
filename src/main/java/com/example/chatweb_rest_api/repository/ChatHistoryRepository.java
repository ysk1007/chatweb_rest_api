package com.example.chatweb_rest_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatweb_rest_api.dto.ChatHistory;
import com.example.chatweb_rest_api.dto.ChatHistoryMapping;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long>{
	Page<ChatHistoryMapping> findByUser_UserNo(Long userNo, Pageable pageable);	// 유저 번호로 저장한 대화 조회 (페이징)
}
