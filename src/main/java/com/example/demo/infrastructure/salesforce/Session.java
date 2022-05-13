package com.example.demo.infrastructure.salesforce;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Data;

@Component
@Scope("singleton")
@Data
public class Session {

	private String accessToken;
	private String instanceUrl;

	public Boolean isLogin() {
		return !StringUtils.isEmpty(this.accessToken) && !StringUtils.isEmpty(this.instanceUrl);
	}
}
