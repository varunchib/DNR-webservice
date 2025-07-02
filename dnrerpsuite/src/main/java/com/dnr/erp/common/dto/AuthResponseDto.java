package com.dnr.erp.common.dto;

public class AuthResponseDto {
	private String token;
	private String email;

	public AuthResponseDto(String token, String email) {
		this.token = token;
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public String getEmail() {
		return email;
	}
}
