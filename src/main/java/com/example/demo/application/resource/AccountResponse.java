package com.example.demo.application.resource;

import com.example.demo.domain.object.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {

	private String id;

	public static AccountResponse build(Account account) {
		return AccountResponse.builder().id(account.getId()).build();
	}

}
