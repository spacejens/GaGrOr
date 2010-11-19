package se.spacejens.gagror.controller.ejb;

import javax.ejb.Singleton;

import se.spacejens.gagror.controller.RequestContext;
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
			throws UserCreationException, RepeatedPasswordNotMatchingException {
		final JpaContext jpa = this.getJpaContext(rc);
		return this.getUserHelper(jpa).registerUser(username, password, repeatPassword);
	}
}
