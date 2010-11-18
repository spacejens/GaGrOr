package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.controller.Encrypter;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.User;

/**
 * Implementation of user helper.
 * 
 * @author spacejens
 */
public class UserHelperImpl extends HelperSupport implements UserHelper {

	/**
	 * Create instance.
	 * 
	 * @param jpa
	 *            JPA context to use.
	 */
	public UserHelperImpl(final JpaContext jpa) {
		super(jpa);
	}

	@Override
	public User registerUser(final String username, final String password) {
		// TODO Verify that the user is not currently logged in
		final String encryptedPassword = Encrypter.encryptPassword(username, password);
		// TODO Flush entitymanager to check if username busy, throw exception
		return this.getUserDAO(this.getJpa()).createUser(username, encryptedPassword);
	}
}
