package com.gagror.data.account;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

interface AccountRepositoryQueries extends Repository<AccountEntity, Long> {

	List<AccountEntity> findAll(final Sort sort);

	AccountEntity findByName(final String name);

	AccountEntity findById(final Long id);

	AccountEntity save(final AccountEntity account);
}
