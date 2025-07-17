package com.example.chatweb_rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatweb_rest_api.dto.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUserId(String userId);
}
