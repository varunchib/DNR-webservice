package com.dnr.erp.modules.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dnr.erp.common.dto.AuthResponseDto;
import com.dnr.erp.common.dto.LoginRequestDto;
import com.dnr.erp.modules.auth.service.AuthService;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {
	
	private final AuthService authService;
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/")
	public String sayHello() {
		return "Hello WOrld";
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
		AuthResponseDto response = authService.login(request);
		if (response != null) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(401).body("Invalid email or password");
		}
	}

}