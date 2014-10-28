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

import com.gagror.data.AbstractEditableEntity;
import com.gagror.data.group.GroupMemberEntity;

@NoArgsConstructor
@ToString(of={"username", "accountType"}, callSuper=true)
@Entity
@Table(name="account")
public class AccountEntity extends AbstractEditableEntity {

	@Column(nullable = false)
	@Getter
	@Setter
	private String username;

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
		setUsername(username);
		setPassword(encryptedPassword);
		setAccountType(AccountType.STANDARD);
		setActive(true);
		setLocked(false);
	}
}
