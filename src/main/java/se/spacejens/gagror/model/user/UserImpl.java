package se.spacejens.gagror.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import se.spacejens.gagror.model.EntitySupport;

/**
 * Implementation of system user.
 * 
 * @author spacejens
 */
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserImpl extends EntitySupport implements User {

	/** The username of this user. */
	private String username;

	/** The encrypted password of this user. */
	private String password;

	@Override
	@NotNull
	@Size(min = User.USERNAME_MIN_LENGTH, max = User.USERNAME_MAX_LENGTH)
	@Column(name = "username", nullable = false, insertable = true, updatable = false)
	public String getUsername() {
		return this.username;
	}

	/**
	 * Set the username. Should only be called by the persistence layer.
	 * 
	 * @param username
	 *            Not null.
	 */
	void setUsername(final String username) {
		this.username = username;
	}

	@Override
	@NotNull
	@Size(min = User.PASSWORD_ENCRYPTED_MIN_LENGTH, max = User.PASSWORD_ENCRYPTED_MAX_LENGTH)
	@Column(name = "password", nullable = false, insertable = true, updatable = true)
	public String getPassword() {
		return this.password;
	}

	@Override
	public void setPassword(final String password) {
		this.password = password;
	}
}
