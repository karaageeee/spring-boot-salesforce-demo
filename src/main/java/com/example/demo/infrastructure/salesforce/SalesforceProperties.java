package com.example.demo.infrastructure.salesforce;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "salesforce")
public class SalesforceProperties {

	private String username;
	private String password;
	private String securityToken;
	private String clientId;
	private String clientSecret;
	
}
