package com.dnr.erp.modules.auth.service;

import com.dnr.erp.common.dto.AuthResponseDto;
import com.dnr.erp.common.dto.LoginRequestDto;
import com.dnr.erp.common.security.JwtUtil;
import com.dnr.erp.modules.auth.entity.User;
import com.dnr.erp.modules.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Autowired
	public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	public AuthResponseDto login(LoginRequestDto request) {
		return userRepository.findByEmail(request.getEmail())
			.filter(user -> user.getPassword().equals(request.getPassword()))
			.map(user -> {
				String token = jwtUtil.generateToken(user.getEmail()); // email as subject
				return new AuthResponseDto(token, user.getEmail());
			})
			.orElse(null);
	}
}
