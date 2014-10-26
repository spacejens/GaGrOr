package com.gagror.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.gagror.data.AbstractEditableEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name="contact")
@EqualsAndHashCode(of={}, callSuper=true)
public class ContactEntity extends AbstractEditableEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private AccountEntity owner;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	private AccountEntity contact;

	@Column(nullable = false)
	private ContactType contactType;

	public ContactEntity(final AccountEntity owner, final ContactType contactType, final AccountEntity contact) {
		setOwner(owner);
		setContactType(contactType);
		setContact(contact);
		// Add the new entity to the referencing collections
		owner.getContacts().add(this);
		contact.getIncomingContacts().add(this);
	}
}
