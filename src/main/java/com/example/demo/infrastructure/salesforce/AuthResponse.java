package com.example.demo.infrastructure.salesforce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String instance_url;
	private String token_type;
	private String access_token;
}
