package com.example.chatweb_rest_api.dto;

import java.time.LocalDateTime;

public interface LoginHistoryMapping {
	Long getHistoryNo();
	LocalDateTime getLoginAt();
	LocalDateTime getLogoutAt();
}
