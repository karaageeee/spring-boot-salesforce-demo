package com.example.demo.infrastructure.salesforce;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.service.AccountService;

@Component
public class OperationsImpl implements Operations {

	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Session sess;

	@Autowired
	private SalesforceProperties sfProps;

	@Value("${spring.profiles.active}")
	private String env;

	static final String SalesforceGrantService = "/services/oauth2/token?grant_type=password";

	public QueryResponse query(String query) {

		logger.info(this.getClass().getName() + " is processing...");

		try {
			this.verifySession();
		} catch (Exception e) {
			logger.error("Cannot establicsh Salesforce session", e.getMessage());
			throw e;
		}

		String url = String.format("%s/services/data/v41.0/query?q=%s", sess.getInstanceUrl(), query);

		final HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", String.format("Bearer %s", sess.getAccessToken()));
		headers.set("Content-Type", "application/json");

		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		
		ResponseEntity<QueryResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, QueryResponse.class);

		return response.getBody();
	}

	private void auth() {

		logger.info("Start Salesforce auth");

		String url = env.toLowerCase() == "production" ? "https://login.salesforce.com" : "https://test.salesforce.com";
		url += SalesforceGrantService;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add("client_id", sfProps.getClientId());
		requestBody.add("client_secret", sfProps.getClientSecret());
		requestBody.add("username", sfProps.getUsername());
		requestBody.add("password", sfProps.getPassword() + sfProps.getSecurityToken());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestBody,
				headers);

		ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() {
		};
		Map<String, String> response = restTemplate.exchange(url, HttpMethod.POST, request, responseType).getBody();

		String accessToken = response.get("access_token");
		String instanceUrl = response.get("instance_url");

		if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(instanceUrl)) {
			throw new Error("Salesforce Auth failed");
		}

		sess.setAccessToken(accessToken);
		sess.setInstanceUrl(instanceUrl);

	}

	private void verifySession() {

		logger.info("Start verify session");

		if (!sess.isLogin()) {
			logger.info("first time auth salesforce");
			this.auth();
			return;
		}

		String url = String.format("%s/services/data/v41.0", sess.getInstanceUrl());
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", String.format("Bearer %s", sess.getAccessToken()));
		headers.set("Content-Type", "application/json");

		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		
		if (HttpStatus.UNAUTHORIZED == response.getStatusCode() ) {
			logger.info("Since Salesforce session expired, setup session again");
			this.auth();
			return;
		} else if (HttpStatus.OK != response.getStatusCode() ) {
			logger.error("Unexpected error, failed to verify salesforce session. StatusCode: ", response.getStatusCode());
			throw new Error("System Error: failed to access salesforce");
		}
		
		logger.info("Session is available");

	}
}
