package com.dnr.erp.modules.auth.service;

import com.dnr.erp.common.dto.AuthResponseDto;
import com.dnr.erp.common.dto.LoginRequestDto;
import com.dnr.erp.common.dto.SignUpRequestDto;
import com.dnr.erp.common.dto.UserSummaryDto;
import com.dnr.erp.common.security.JwtUtil;
import com.dnr.erp.common.security.Role;
import com.dnr.erp.modules.auth.entity.User;
import com.dnr.erp.modules.auth.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
	
	public void setTokenCookie(HttpServletResponse response, String token) {
	    ResponseCookie cookie = ResponseCookie.from("token", token)
	        .httpOnly(true)
	        .secure(true) // only in HTTPS (disable for localhost dev if needed)
	        .sameSite("None")
	        .path("/")
	        .maxAge(3600) // 1 hour
	        .build();

	    response.addHeader("Set-Cookie", cookie.toString());
	}
	
	public void clearTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0)
            .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

	public AuthResponseDto login(LoginRequestDto request) {
		return userRepository.findByEmail(request.getEmail())
			.filter(user -> user.getPassword().equals(request.getPassword()))
			.map(user -> {
				String token = jwtUtil.generateToken(user.getId(), user.getRole(), user.getEmail(), user.getFullName()); // email as subject
				return new AuthResponseDto(token, user.getEmail(), user.getFullName(), user.getRole().name());
			})
			.orElse(null);
	}
	
	public ResponseEntity<String> signup(SignUpRequestDto request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			return ResponseEntity.status(409).body("Email already in use");
		}
		
		if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
			return ResponseEntity.status(409).body("Employee ID already in use");
		}
		
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setFullName(request.getFullName());
		user.setRole(Role.valueOf(request.getRole()));
		user.setEmployeeId(request.getEmployeeId());
		
		userRepository.save(user);		
		return ResponseEntity.ok("Signup successful");
	}
	
	public List<Map<String, Object>> getAllUserDetailsForAdmin() {
	    return userRepository.findAll().stream()
	        .map(user -> {
	            Map<String, Object> map = new HashMap<>();
	            map.put("fullName", user.getFullName());
	            map.put("email", user.getEmail());
	            map.put("employeeId", user.getEmployeeId()); // Object type
	            map.put("role", user.getRole().name());
	            return map;
	        })
	        .collect(Collectors.toList());
	}
	
	public boolean deleteUserById(UUID id) {
	    Optional<User> user = userRepository.findById(id);
	    if (user.isPresent()) {
	        userRepository.deleteById(id);
	        return true;
	    }
	    return false;
	}


}
