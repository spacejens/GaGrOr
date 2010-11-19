package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.controller.Encrypter;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.User;
import se.spacejens.gagror.model.user.UserCreationException;

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
	public User registerUser(final String username, final String password, final String repeatPassword) throws UserCreationException,
			RepeatedPasswordNotMatchingException {
		if (!password.equals(repeatPassword)) {
			throw new RepeatedPasswordNotMatchingException();
		}
		// TODO Verify that the user is not currently logged in
		final String encryptedPassword = Encrypter.encryptPassword(username, password);
		return this.getUserDAO(this.getJpa()).createUser(username, encryptedPassword);
	}
}
