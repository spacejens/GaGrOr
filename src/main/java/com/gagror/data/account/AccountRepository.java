package com.gagror.data.account;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

public interface AccountRepository extends Repository<AccountEntity, Long> {

	Iterable<AccountEntity> findAll(final Sort sort);

	AccountEntity findByName(final String name);

	AccountEntity findById(final Long id);

	AccountEntity save(final AccountEntity account);
}
