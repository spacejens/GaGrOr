package com.gagror.data.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.gagror.data.DataNotFoundException;

@Repository
public class AccountRepository {

	@Autowired
	AccountRepositoryQueries accountRepositoryQueries;

	private static final Sort SORT_BY_NAME = new Sort("name");

	public List<AccountEntity> list() {
		return accountRepositoryQueries.findAll(SORT_BY_NAME);
	}

	public boolean exists(final String name) {
		return null != accountRepositoryQueries.findByName(name);
	}

	public AccountEntity loadOrNull(final String name) {
		// Allowed to return null because of special usage for access control
		return accountRepositoryQueries.findByName(name);
	}

	public AccountEntity load(final Long accountId) {
		final AccountEntity account = accountRepositoryQueries.findById(accountId);
		if(null == account) {
			throw new DataNotFoundException(String.format("Account %d", accountId));
		}
		return account;
	}

	public AccountEntity persist(final AccountEntity account) {
		return accountRepositoryQueries.save(account);
	}
}
