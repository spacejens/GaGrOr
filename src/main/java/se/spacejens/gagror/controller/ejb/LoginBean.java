package se.spacejens.gagror.controller.ejb;

import javax.ejb.Singleton;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.User;

/**
 * Implementation of login service.
 * 
 * @author spacejens
 */
@Singleton
public class LoginBean extends EJBSupport implements LoginService {

	@Override
	public User registerUser(final RequestContext rc, final String username, final String password) throws GagrorException {
		final JpaContext jpa = this.getJpaContext(rc);
		return this.getUserHelper(jpa).registerUser(username, password);
	}
}
