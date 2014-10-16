package com.gagror.data.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@ToString(exclude="password")
@Table(name="account")
public class AccountEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Version
	private Long version;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private AccountType accountType;

	public AccountEntity(final String username, final String encryptedPassword) {
		setUsername(username);
		setPassword(encryptedPassword);
		setAccountType(AccountType.STANDARD);
	}
}
