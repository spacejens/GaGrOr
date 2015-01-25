package com.gagror.data.account;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.gagror.data.AbstractEditableNamedEntity;
import com.gagror.data.group.GroupMemberEntity;

@NoArgsConstructor
@ToString(of={"accountType"}, callSuper=true)
@Entity
@Table(name="gagror_account")
public class AccountEntity extends AbstractEditableNamedEntity {

	@Column(nullable = false)
	@Getter
	@Setter
	private String password;

	@Column(nullable = false)
	@Getter
	@Setter
	private AccountType accountType;

	@Column(nullable = false)
	@Getter
	@Setter
	private boolean active;

	@Column(nullable = false)
	@Getter
	@Setter
	private boolean locked;

	@OneToMany(mappedBy="owner", fetch=FetchType.LAZY)
	@Getter
	private Set<ContactEntity> contacts;

	@OneToMany(mappedBy="contact", fetch=FetchType.LAZY)
	@Getter
	private Set<ContactEntity> incomingContacts;

	@OneToMany(mappedBy="account", fetch=FetchType.LAZY)
	@Getter
	private Set<GroupMemberEntity> groupMemberships;

	public AccountEntity(final String username, final String encryptedPassword) {
		setName(username);
		setPassword(encryptedPassword);
		setAccountType(AccountType.STANDARD);
		setActive(true);
		setLocked(false);
	}
}
