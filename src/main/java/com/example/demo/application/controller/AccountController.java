package com.example.demo.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.resource.AccountResponse;
import com.example.demo.domain.object.Account;
import com.example.demo.domain.service.AccountService;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {
	
	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/first")
	public AccountResponse getFirst() {
		
		logger.info(this.getClass().getName() +  " is processing...");
		
		Account account = accountService.getFirst();
		return AccountResponse.build(account);
	}

}
