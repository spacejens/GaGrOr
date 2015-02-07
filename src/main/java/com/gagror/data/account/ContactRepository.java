package com.gagror.data.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ContactRepository {

	@Autowired
	ContactRepositoryQueries contactRepositoryQueries;

	public ContactEntity persist(final ContactEntity contact) {
		return contactRepositoryQueries.save(contact);
	}

	public void delete(final ContactEntity contact) {
		contactRepositoryQueries.delete(contact);
	}
}
