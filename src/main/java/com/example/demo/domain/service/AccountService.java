package com.example.demo.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.object.Account;
import com.example.demo.domain.repository.AccountRepository;

@Service
public class AccountService {
	
	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AccountRepository accountRepository;
	
	public Account getFirst() {
		
		logger.info(this.getClass().getName() +  " is processing...");
		
		return accountRepository.getFirst();
	}
}
