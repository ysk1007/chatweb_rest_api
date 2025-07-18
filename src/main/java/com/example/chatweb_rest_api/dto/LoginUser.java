package com.example.chatweb_rest_api.dto;

import jakarta.servlet.http.HttpSessionBindingListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser implements HttpSessionBindingListener {
	
    private Long userNo;
    private String userId;
    private String userName;
    private String userRole;
    private String userGender;
    private String userBirth;

    public LoginUser(User user) {
        this.userNo = user.getUserNo();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userRole = user.getUserRole();
        this.userGender = user.getUserGender();
        this.userBirth = user.getUserBirth();
    }
}
