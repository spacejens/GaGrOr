package com.gagror.data.account;

import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<AccountEntity, Long> {

	AccountEntity findByLoginAndPassword(final String login, final String password);
}
