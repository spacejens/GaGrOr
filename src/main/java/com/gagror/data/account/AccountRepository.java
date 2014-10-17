package com.gagror.data.account;

import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<AccountEntity, Long> {

	Iterable<AccountEntity> findAll();

	AccountEntity findByUsername(final String username);

	AccountEntity findById(final Long id);

	AccountEntity save(final AccountEntity account);
}
