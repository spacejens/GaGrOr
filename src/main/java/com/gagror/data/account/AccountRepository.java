package com.gagror.data.account;

import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<AccountEntity, Long> {

	AccountEntity findByUsername(final String username);

	AccountEntity findByUsernameAndPassword(final String username, final String password);

	AccountEntity save(final AccountEntity account);
}
