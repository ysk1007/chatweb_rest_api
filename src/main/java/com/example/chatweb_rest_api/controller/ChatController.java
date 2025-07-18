package com.example.chatweb_rest_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatweb_rest_api.dto.ChatHistory;
import com.example.chatweb_rest_api.dto.LoginUser;
import com.example.chatweb_rest_api.dto.User;
import com.example.chatweb_rest_api.repository.ChatHistoryRepository;
import com.example.chatweb_rest_api.service.AiChatService;
import com.example.chatweb_rest_api.service.ChatService;

import jakarta.servlet.http.HttpSession;

@RestController
public class ChatController {

    private final ChatHistoryRepository chatHistoryRepository;
	
	private final AiChatService aiChatService;
	private final ChatService chatService;
	
	public ChatController(AiChatService aiChatService, ChatService chatService, ChatHistoryRepository chatHistoryRepository) {
		this.aiChatService = aiChatService;
		this.chatService = chatService;
		this.chatHistoryRepository = chatHistoryRepository;
	}
	
	// 채팅 보내고 응답 받는 api
	@PostMapping("/chat")
	public ResponseEntity<String> chat(@RequestBody Map<String, String> body, HttpSession session) {	// session 속성안에 message List를 만들어 이전대화를 누적
		
		// {"userMsg":"hello","":""} Json 문자열 -> 자바 DTO 객체(@RequestBody)
		String userMsg = body.get("userMsg");	
		String aiReply = aiChatService.generate(userMsg,session);
		System.out.println("ChatController : 응답 합니다 =>" + aiReply);
		if(!aiReply.isEmpty()) {			
			return new ResponseEntity<String>(aiReply, HttpStatus.OK);
		}
		return new ResponseEntity<String>("오류가 발생 했습니다."	, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 현재까지 대화(세션에 있는) 내용 가져오기
 	@GetMapping("/getChatHistory")
    public List<Message> getChatHistory(HttpSession session) {
        List<Message> chatHistory = (List<Message>) session.getAttribute("chatHistory");
        if (chatHistory == null) {
            chatHistory = new ArrayList<>();
        }
     // system 메시지 제외
        return chatHistory.stream()
                .filter(msg -> !"SYSTEM".equalsIgnoreCase(msg.getMessageType().toString()))
                .collect(Collectors.toList());
    }
	
	// 현재까지 대화(세션에 있는) 내용 저장하기
	@PostMapping("/chatHistorySave")
	public ResponseEntity<String> chatHistorySave(HttpSession session) {	// session 속성안에 message List를 만들어 이전대화를 누적
		
		List<Message> messageList = (List<Message>)session.getAttribute("chatHistory");
		
		ChatHistory chatHistory = new ChatHistory();
		for(Message m : messageList) {
			// 디버깅
			System.out.println("ChatController MessageType: " + m.getMessageType().toString());
			System.out.println("ChatController Text: " + m.getText().toString());
			
			String messageType = m.getMessageType().toString();		// 메세지 타입 (SYSTEM, USER, ASSISTANT)
			String msg = m.getText().toString();					// 저장할 문자
			if(messageType.equals("SYSTEM")) continue;
			
			if(messageType.equals("USER")) {
				LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
				User user = new User(loginUser);
				chatHistory.setUser(user);
				chatHistory.setUserMsg(msg);
				chatHistory.setBookmark(0);
				chatHistory.setSessionId(session.getId());
			}
			else if(messageType.equals("ASSISTANT")) {
				chatHistory.setAiReply(msg);
				chatService.addChatHistory(chatHistory);	// 저장
				
				chatHistory = new ChatHistory();			// 초기화
			}
		}
		
		//chatService.addChatHistory(null);
		return new ResponseEntity<String>("채팅 내용을 저장 했습니다!", HttpStatus.OK);
	}
	
	// 세션에 있는 대화 내용 리셋하기
	@PostMapping("/chatHistoryReset")
	public ResponseEntity<String> chatHistoryReset(HttpSession session) {
		List<Message> messageList = new ArrayList<Message>();
		session.setAttribute("chatHistory", messageList);
		
		return new ResponseEntity<String>("현재 세션 대화를 리셋 했습니다!", HttpStatus.OK);
	}
}
