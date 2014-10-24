package com.gagror.data.account;

import org.springframework.data.repository.Repository;

public interface ContactRepository extends Repository<ContactEntity, Long> {

	ContactEntity save(final ContactEntity contact);

	void delete(final ContactEntity contact);
}
