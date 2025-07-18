package com.example.chatweb_rest_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatweb_rest_api.dto.LoginHistoryMapping;
import com.example.chatweb_rest_api.dto.LoginUser;
import com.example.chatweb_rest_api.dto.User;
import com.example.chatweb_rest_api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	// 로그인
	@PostMapping("/signIn")
	public ResponseEntity<String> signIn(@RequestBody User user, HttpServletRequest request) {
        User login = userService.login(user);
        if (login != null) {
        	LoginUser loginUser = new LoginUser(login);
        	HttpSession session = request.getSession(true);	// 세션 생성
        	session.setAttribute("loginUser", loginUser);
        	userService.addLoginHistory(session);
            return new ResponseEntity<String>("로그인 성공", HttpStatus.OK);
        }
        return new ResponseEntity<String>("로그인 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
	    request.getSession().invalidate();
	    System.out.println("UserController : 로그아웃 하고 세션 제거 합니다.");
	    return ResponseEntity.ok().build();
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
	
	// 사용자 정보 조회
	@GetMapping("/api/userInfo")
	@ResponseBody
	public User getUserInfo(HttpSession session) {
	    LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return null; // 또는 적절한 예외/응답 처리
	    }
	    User user = new User(loginUser);
	    return user;
	}
	
	// 로그인 이력 조회
	@GetMapping("/loginHistory/{userNo}/{page}")
	public Page<LoginHistoryMapping> getLoginHistories(
			@PathVariable Long userNo,
	        @PathVariable int page) {
	    return userService.getLoginHistoryList(userNo, page);
	}
}
