package com.example.demo.infrastructure.repository;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.domain.object.Account;
import com.example.demo.domain.repository.AccountRepository;
import com.example.demo.domain.service.AccountService;
import com.example.demo.infrastructure.salesforce.Operations;
import com.example.demo.infrastructure.salesforce.QueryResponse;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
	
	
	private final static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private Operations sfOperations;
	
	public Account getFirst() {
		
		logger.info(this.getClass().getName() +  " is processing...");
		
		QueryResponse res = sfOperations.query("SELECT ID FROM Account LIMIT 1");

		logger.info(res.toString());
		
		if (res.getTotalSize() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		HashMap<String, Object> record = res.getRecords().get(0);
		return Account.builder().id(record.get("Id").toString()).build();
	}
	
}
