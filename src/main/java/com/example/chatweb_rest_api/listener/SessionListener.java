package com.example.chatweb_rest_api.listener;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.chatweb_rest_api.dto.LoginUser;
import com.example.chatweb_rest_api.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Component
public class SessionListener implements HttpSessionListener {

    private final UserService userService;

    @Autowired
    public SessionListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("SessionListener : 세션 생성됨");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("SessionListener : 세션 종료됨");

        HttpSession session = se.getSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        if (loginUser != null) {
            Long historyNo = (Long) session.getAttribute("historyNo");
            userService.addLogoutHistory(historyNo, loginUser);
        }
    }
}
