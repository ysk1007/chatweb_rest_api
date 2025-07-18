package com.example.chatweb_rest_api.dto;

import java.time.LocalDateTime;

public interface ChatHistoryMapping {
	Long getChatNo();
	String getUserMsg();
	String getAiReply();
	Integer getBookmark();
	LocalDateTime getCreateAt();
	String getSessionId();
}
