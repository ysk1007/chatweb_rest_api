package com.example.chatweb_rest_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatweb_rest_api.dto.LoginHistory;
import com.example.chatweb_rest_api.dto.LoginHistoryMapping;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
	Page<LoginHistoryMapping> findByUser_UserNo(Long userNo, Pageable pageable);	// 유저 번호로 로그인 이력 조회 (페이징)
}
