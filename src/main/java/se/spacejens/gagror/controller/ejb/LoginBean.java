package se.spacejens.gagror.controller.ejb;

import javax.ejb.Singleton;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
import se.spacejens.gagror.controller.NotLoggedInException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.controller.helper.RepeatedPasswordNotMatchingException;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.User;
import se.spacejens.gagror.model.user.UserCreationException;

/**
 * Implementation of login service.
 * 
 * @author spacejens
 */
@Singleton
public class LoginBean extends EJBSupport implements LoginService {

	@Override
	public User registerUser(final RequestContext rc, final String username, final String password, final String repeatPassword)
			throws UserCreationException, RepeatedPasswordNotMatchingException, MayNotBeLoggedInException {
		try {
			final JpaContext jpa = this.getJpaContext(rc);
			return this.getUserHelper(jpa).registerUser(username, password, repeatPassword);
		} catch (LoginFailedException e) {
			throw new MayNotBeLoggedInException(e);
		}
	}

	@Override
	public User loginUser(final RequestContext rc, final String username, final String password) throws LoginFailedException {
		final JpaContext jpa = this.getJpaContext(rc);
		return this.getUserHelper(jpa).loginUser(username, password);
	}

	@Override
	public User verifyLogin(final RequestContext rc) throws ServiceCommunicationException, NotLoggedInException, LoginFailedException {
		final JpaContext jpa = this.getJpaContext(rc);
		final User user = jpa.getCurrentUser();
		if (null == user) {
			throw new NotLoggedInException();
		}
		return user;
	}
}
