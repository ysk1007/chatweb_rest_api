package com.example.chatweb_rest_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.chatweb_rest_api.dto.LoginHistoryMapping;
import com.example.chatweb_rest_api.dto.User;
import com.example.chatweb_rest_api.repository.LoginHistoryRepository;
import com.example.chatweb_rest_api.repository.UserRepository;

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
		int pageSize = 10;
	    int pageNumber = currentPage - 1;

	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("historyNo").descending());
	    Page<LoginHistoryMapping> historyList = loginHistoryRepository.findByUser_UserNo(userNo, pageable);
	    System.out.println("asdfasdfasdfadsf"+historyList.toString());
	    
	    return historyList;
	}
}
