package com.gagror.data.account;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.group.GroupMemberEntity;

@Data
@NoArgsConstructor
@Entity
@ToString(exclude="password")
@Table(name="account")
@EqualsAndHashCode(of={}, callSuper=true)
public class AccountEntity extends AbstractEditableEntity {

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private AccountType accountType;

	@Column(nullable = false)
	private boolean active;

	@Column(nullable = false)
	private boolean locked;

	@Column(nullable = false, insertable = true, updatable = false)
	private Date created;

	@OneToMany(mappedBy="owner", fetch=FetchType.LAZY)
	private Set<ContactEntity> contacts;

	@OneToMany(mappedBy="contact", fetch=FetchType.LAZY)
	private Set<ContactEntity> incomingContacts;

	@OneToMany(mappedBy="account", fetch=FetchType.LAZY)
	private Set<GroupMemberEntity> groupMemberships;

	public AccountEntity(final String username, final String encryptedPassword) {
		setUsername(username);
		setPassword(encryptedPassword);
		setAccountType(AccountType.STANDARD);
		setActive(true);
		setLocked(false);
		setCreated(new Date());
	}
}
