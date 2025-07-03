package com.dnr.erp.modules.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnr.erp.common.dto.AuthResponseDto;
import com.dnr.erp.common.dto.LoginRequestDto;
import com.dnr.erp.common.dto.SignUpRequestDto;
import com.dnr.erp.modules.auth.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
	
	private final AuthService authService;
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/")
	public String sayHello() {
		return "Hello Kitty...🐱";
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {
	    AuthResponseDto auth = authService.login(request);
	    if (auth != null) {
	        authService.setTokenCookie(response, auth.getToken()); // ⬅️ attach cookie
	        return ResponseEntity.ok("Login successful");
	    }
	    return ResponseEntity.status(401).body("Invalid email or password");
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody SignUpRequestDto request) {
		return authService.signup(request);
	}

	

}