package com.example.chatweb_rest_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import com.example.chatweb_rest_api.dto.ChatHistory;
import com.example.chatweb_rest_api.dto.LoginUser;
import com.example.chatweb_rest_api.dto.User;

import jakarta.servlet.http.HttpSession;

@Service
public class AiChatService {
	private OpenAiChatModel openAiChatModel;
	private ChatService chatService;
	
	public AiChatService(OpenAiChatModel openAiChatModel, ChatService chatService) {
		this.openAiChatModel = openAiChatModel;
		this.chatService = chatService;
	}
	
	// OpenAI 서버와 통신할 메서드 선언
	// param String userMsg : 사용자 문자열 메세지
	// return String : 오픈 챗 서버의 응답 문자열
	public String generate(String userMsg, HttpSession session) {
		/*
		 *  DB 저장될 내용
		 *  
		 *  key auto_increment
		 *  session.getId()
		 *  userMsg
		 *  aiReply
		 *  
		 *  ex)
		 *  1, t45tr23e12e23yv124, 안녕, 그래 무엇을 도와줄까  
		 */
		
		
		List<Message> messageList = (List<Message>)session.getAttribute("chatHistory");
		
		if(messageList == null) {
			messageList = new ArrayList<Message>();	// 이전 이력 없이 처음 대화이다.
		}		
		
		// SystemMessage, UserMessage
		SystemMessage systemMessage = new SystemMessage("너는 무엇이든지 친절하게 답변 해주는 친절한 ai 챗봇이야");
		UserMessage userMessage = new UserMessage(userMsg);
		
		messageList.add(systemMessage);
		messageList.add(userMessage);
		
		
		// 옵션
	    OpenAiChatOptions options = OpenAiChatOptions.builder()
	            .model("gpt-3.5-turbo") // 사용하고자 하는 OpenAI 모델(버전)의 이름을 지정
	            .temperature(0.7) // 창의성(무작위성) 정도를 설정(0.0 ~ 2.0)값으로 보통 0~1 사이 사용
	            .build();
		
	    
	    // OpenAI 서버에 전달되는 모델 객체(매개값)
	    Prompt prompt = new Prompt(messageList, options);
	    
	    // openAiChatModel 빈을 통해 prompt를 OpenAi 서버에 전달
	    ChatResponse res = this.openAiChatModel.call(prompt);
	    String aiReply = res.getResult().getOutput().getText();
	    
	    // AI 응답도 messageList에 누적
	    AssistantMessage assistantMessage = new AssistantMessage(aiReply);
	    messageList.add(assistantMessage);
	    
	    // messageList 변경된 내용 session의 messageList 속성에도 반영
	    session.setAttribute("chatHistory", messageList);	// 이전 session chatHistory 속성값을 덮어쓰기
	    
	    // DAO 호출
	    ChatHistory chatHistory = new ChatHistory();
	    LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
	    User user = new User(loginUser);
	    chatHistory.setUser(user);
	    chatHistory.setUserMsg(userMsg);
	    chatHistory.setAiReply(aiReply);
	    chatHistory.setSessionId(session.getId());
	    
		return aiReply;
	}
}
