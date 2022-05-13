package com.example.demo.domain.repository;

import com.example.demo.domain.object.Account;

public interface AccountRepository {
	
	/**
	 * @return Account
	 */
	public Account getFirst();

}
