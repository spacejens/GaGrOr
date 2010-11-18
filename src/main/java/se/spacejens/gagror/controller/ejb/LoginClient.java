package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.GagrorException;
import se.spacejens.gagror.controller.NamingContextProvider;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.model.user.User;

/**
 * Login service client.
 * 
 * @author spacejens
 */
public class LoginClient extends EJBClientSupport<LoginService> implements LoginService {

	/**
	 * Create client instance.
	 * 
	 * @param namingContextProvider
	 *            Used for JNDI lookup.
	 */
	public LoginClient(final NamingContextProvider namingContextProvider) {
		super(namingContextProvider);
	}

	@Override
	public User registerUser(final RequestContext rc, final String username, final String password) throws GagrorException {
		return this.getReference().registerUser(rc, username, password);
	}

	@Override
	protected String getBeanName() {
		return LoginBean.class.getSimpleName();
	}
}
