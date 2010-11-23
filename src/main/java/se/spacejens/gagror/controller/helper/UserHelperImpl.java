package se.spacejens.gagror.controller.helper;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
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
			RepeatedPasswordNotMatchingException, MayNotBeLoggedInException {
		if (this.getJpa().isContainingLoginInformation()) {
			throw new MayNotBeLoggedInException();
		}
		if (!password.equals(repeatPassword)) {
			throw new RepeatedPasswordNotMatchingException();
		}
		final String encryptedPassword = this.getEncryptionHelper(this.getJpa()).encryptPassword(username, password);
		return this.getUserDAO(this.getJpa()).createUser(username, encryptedPassword);
	}

	@Override
	public User loginUser(final String username, final String password) throws LoginFailedException {
		final User user = this.getUserDAO(this.getJpa()).findUser(username,
				this.getEncryptionHelper(this.getJpa()).encryptPassword(username, password));
		if (null == user) {
			throw new LoginFailedException();
		}
		return user;
	}
}
