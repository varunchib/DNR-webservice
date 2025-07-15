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
import com.dnr.erp.common.security.UserPrincipal;
import com.dnr.erp.modules.auth.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${frontend.origin}", allowCredentials = "true")
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

	    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

	    Map<String, Object> userData = Map.of(
	        "id", principal.getUserId(),
	        "email", principal.getEmail(),
	        "fullName", principal.getName(),
	        "role", principal.getRole().name()
	    );

	    return ResponseEntity.ok(Map.of(
	        "message", "Authorized",
	        "user", userData
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
	public ResponseEntity<String> logout(HttpServletResponse response) {
		authService.clearTokenCookie(response);
		return ResponseEntity.ok("Logged out");
	}	

}