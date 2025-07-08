package com.dnr.erp.modules.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/verify")
	public ResponseEntity<?> verify(Authentication authentication) {
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(401).body(Map.of(
	            "message", "Unauthorized"
	        ));
	    }

	    return ResponseEntity.ok(Map.of(
	        "message", "Authorized"
//	        "userId", authentication.getPrincipal(),
//	        "roles", authentication.getAuthorities()
	    ));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {
	    AuthResponseDto auth = authService.login(request);
	    if (auth != null) {
	        authService.setTokenCookie(response, auth.getToken()); 
	        return ResponseEntity.ok("Login successful");
	    }
	    return ResponseEntity.status(401).body("Invalid email or password");
	}
	
	@PostMapping("/signup")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> signup(@RequestBody SignUpRequestDto request) {
		return authService.signup(request);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletResponse reponse) {
		authService.clearTokenCookie(reponse);
		return ResponseEntity.ok("Logged out");
	}	

}