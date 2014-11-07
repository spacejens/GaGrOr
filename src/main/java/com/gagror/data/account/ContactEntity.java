package com.gagror.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;

@NoArgsConstructor
@ToString(of={"owner", "contact", "contactType"}, callSuper=true)
@Entity
@Table(name="contact")
public class ContactEntity extends AbstractEditableEntity {

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private AccountEntity owner;

	@ManyToOne(optional=false)
	@JoinColumn(nullable=false, insertable=true, updatable=false)
	@Getter
	private AccountEntity contact;

	@Column(nullable = false)
	@Getter
	@Setter
	private ContactType contactType;

	public ContactEntity(final AccountEntity owner, final ContactType contactType, final AccountEntity contact) {
		this.owner = owner;
		setContactType(contactType);
		this.contact = contact;
		// Add the new entity to the referencing collections
		owner.getContacts().add(this);
		contact.getIncomingContacts().add(this);
	}
	// TODO Add database constraint to prevent contacting yourself
	// TODO Add database unique constraint to prevent duplicate contacts
}
