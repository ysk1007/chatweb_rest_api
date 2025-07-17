package com.example.chatweb_rest_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatweb_rest_api.dto.LoginHistoryMapping;
import com.example.chatweb_rest_api.dto.User;
import com.example.chatweb_rest_api.service.UserService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")	// react에서 REST 호출
@RestController
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	// 로그인
	@PostMapping("/signIn")
	public ResponseEntity<String> signIn(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user);
        if (loginUser != null) {
        	session.setAttribute("loginUser", loginUser);        	
            return new ResponseEntity<String>("로그인 성공", HttpStatus.OK);
        }
        return new ResponseEntity<String>("로그인 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	// 회원가입
	@PostMapping("/signUp")
	public ResponseEntity<String> signUp(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<String>("회원가입 성공", HttpStatus.OK);
    }
	
	// 아이디 중복 조회
	@GetMapping("/checkId")
	public ResponseEntity<String> checkId(@RequestParam String userId) {
        if(userService.checkId(userId)) {        	
        	return new ResponseEntity<String>("사용 가능한 아이디", HttpStatus.OK);
        }
    	return new ResponseEntity<String>("이미 사용중인 아이디", HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@GetMapping("/api/userInfo")
	@ResponseBody
	public User getUserInfo(HttpSession session) {
	    User user = (User) session.getAttribute("loginUser");
	    System.out.println("현재 로그인된 아이디" + user.toString());
	    return user;
	}
	
	@GetMapping("/loginHistory/{userNo}/{page}")
	public Page<LoginHistoryMapping> getLoginHistories(
			@PathVariable Long userNo,
	        @PathVariable int page) {
	    return userService.getLoginHistoryList(userNo, page);
	}
}
