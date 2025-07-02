package com.dnr.erp.modules.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnr.erp.common.dto.LoginRequestDto;
import com.dnr.erp.modules.auth.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	
	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public boolean Login(LoginRequestDto request) {
		return userRepository.findByEmail(request.getEmail())
				.map(user -> user.getPassword().equals(request.getPassword()))
				.orElse(false);
	}
}
