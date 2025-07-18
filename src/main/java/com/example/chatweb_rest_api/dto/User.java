package com.example.chatweb_rest_api.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "\"user\"")
public class User {
	
	// 기본 생성자 추가 (명시적 혹은 lombok @NoArgsConstructor)
    public User() {}

    public User(LoginUser loginUser) {
        this.userId = loginUser.getUserId();
        this.userName = loginUser.getUserName();
        this.userGender = loginUser.getUserGender();
        this.userNo = loginUser.getUserNo();
        this.userRole = loginUser.getUserRole();
        this.userBirth = loginUser.getUserBirth();
    }
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    private String userId;
    private String userPassword;
    private String userName;
    private String userRole;
    private String userGender;
    private String userBirth;
}
