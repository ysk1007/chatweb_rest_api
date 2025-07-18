package com.example.chatweb_rest_api.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.chatweb_rest_api.dto.LoginHistory;
import com.example.chatweb_rest_api.dto.LoginHistoryMapping;
import com.example.chatweb_rest_api.dto.LoginUser;
import com.example.chatweb_rest_api.dto.User;
import com.example.chatweb_rest_api.repository.LoginHistoryRepository;
import com.example.chatweb_rest_api.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final LoginHistoryRepository loginHistoryRepository;
	
	public UserService(UserRepository userRepository, LoginHistoryRepository loginHistoryRepository) {
		this.userRepository = userRepository;
		this.loginHistoryRepository = loginHistoryRepository;
	}

	// 회원 가입
	public User addUser(User user) {
		return userRepository.save(user);
	}
	
	// 로그인
	public User login(User user) {
		User loginUser = userRepository.findByUserId(user.getUserId()); // userId로 User 조회
	    if (loginUser != null && loginUser.getUserPassword().equals(user.getUserPassword())) {
	        return loginUser; // 로그인 성공, User 반환
	    }
	    return null; // 로그인 실패
	}
	
	// 중복조회
	public Boolean checkId(String userId) {
		User selectUser = userRepository.findByUserId(userId);
		if(selectUser != null) {
			return false;	// 중복
		}
		return true;		// 사용 가능한 아이디
	}
	
	// 로그인 이력 조회
	public Page<LoginHistoryMapping> getLoginHistoryList(Long userNo, int currentPage){
		int pageSize = 5;
	    int pageNumber = currentPage - 1;

	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("historyNo").descending());
	    Page<LoginHistoryMapping> historyList = loginHistoryRepository.findByUser_UserNo(userNo, pageable);
	    
	    return historyList;
	}
	
	// 로그인 이력 발생
	public void addLoginHistory(HttpSession session) {
	    LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
	    if (loginUser == null) return;

	    LoginHistory loginHistory = new LoginHistory();
	    User user = new User(loginUser);

	    long creationTime = session.getCreationTime(); // milliseconds
	    LocalDateTime loginAt = Instant.ofEpochMilli(creationTime)
	                                   .atZone(ZoneId.of("Asia/Seoul"))
	                                   .toLocalDateTime();

	    loginHistory.setLoginAt(loginAt);
	    loginHistory.setUser(user);

	    Long historyNo = loginHistoryRepository.save(loginHistory).getHistoryNo();
	    session.setAttribute("historyNo", historyNo); // 나중에 종료할 때 쓸 수 있게
	}

	
	// 로그아웃 이력 발생
	public void addLogoutHistory(Long historyNo, LoginUser loginUser) {
	    if (historyNo == null) return;

	    Optional<LoginHistory> optional = loginHistoryRepository.findById(historyNo);
	    if (optional.isPresent()) {
	        LoginHistory history = optional.get();

	        // Asia/Seoul 기준 로그아웃 시간
	        LocalDateTime logoutAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	        history.setLogoutAt(logoutAt);

	        User user = new User(loginUser);
	        history.setUser(user);

	        loginHistoryRepository.save(history);
	    }
	}

}
