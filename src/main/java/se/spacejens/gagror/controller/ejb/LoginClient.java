package se.spacejens.gagror.controller.ejb;

import se.spacejens.gagror.controller.LoginFailedException;
import se.spacejens.gagror.controller.MayNotBeLoggedInException;
import se.spacejens.gagror.controller.NamingContextProvider;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ServiceCommunicationException;
import se.spacejens.gagror.controller.helper.RepeatedPasswordNotMatchingException;
import se.spacejens.gagror.model.user.User;
import se.spacejens.gagror.model.user.UserCreationException;

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
	public User registerUser(final RequestContext rc, final String username, final String password, final String repeatPassword)
			throws UserCreationException, ServiceCommunicationException, RepeatedPasswordNotMatchingException, MayNotBeLoggedInException {
		return this.getReference().registerUser(rc, username, password, repeatPassword);
	}

	@Override
	protected String getBeanName() {
		return LoginBean.class.getSimpleName();
	}

	@Override
	public User loginUser(final RequestContext rc, final String username, final String password) throws ServiceCommunicationException,
			LoginFailedException {
		return this.getReference().loginUser(rc, username, password);
	}
}
