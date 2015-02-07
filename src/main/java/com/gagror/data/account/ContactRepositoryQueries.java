package com.gagror.data.account;

import org.springframework.data.repository.Repository;

interface ContactRepositoryQueries extends Repository<ContactEntity, Long> {

	ContactEntity save(final ContactEntity contact);

	void delete(final ContactEntity contact);
}
