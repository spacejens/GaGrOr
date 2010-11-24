package se.spacejens.gagror.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import se.spacejens.gagror.LogAwareSupport;
import se.spacejens.gagror.controller.NamingContextProvider;
import se.spacejens.gagror.controller.RequestContext;
import se.spacejens.gagror.controller.ejb.LoginClient;
import se.spacejens.gagror.controller.ejb.LoginService;
import se.spacejens.gagror.model.user.User;

/**
 * Superclass for all views, providing shared functionality.
 * 
 * @author spacejens
 */
public abstract class ViewSupport extends LogAwareSupport {

	/** Naming context provider to use when looking up services. */
	private NamingContextProvider namingContextProvider = null;

	/** Login service to use. */
	private LoginService loginService = null;

	/**
	 * Get the login service to use.
	 * 
	 * @return Not null.
	 */
	protected LoginService getLoginService() {
		if (null == this.loginService) {
			this.setLoginService(new LoginClient(this.getNamingContextProvider()));
		}
		return this.loginService;
	}

	/**
	 * Set the login service to use.
	 * 
	 * @param loginService
	 *            Not null.
	 */
	void setLoginService(final LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * Extract request context information from a servlet request.
	 * 
	 * @param request
	 *            Not null.
	 * @return New context instance, not null.
	 */
	protected RequestContext getContext(final HttpServletRequest request) {
		return new WebRequestContext(request);
	}

	/**
	 * Store login information for the specified user in the HTTP session.
	 * 
	 * @param user
	 *            Not null.
	 * @param session
	 *            Not null.
	 */
	protected void setLoggedInUser(final User user, final HttpSession session) {
		if (null == user) {
			this.getLog().info("Logged out");
			session.setAttribute(WebSessionAttributes.USERNAME, null);
			session.setAttribute(WebSessionAttributes.PASSWORD, null);
		} else {
			this.getLog().info("Logged in as {}", user.getUsername());
			session.setAttribute(WebSessionAttributes.USERNAME, user.getUsername());
			session.setAttribute(WebSessionAttributes.PASSWORD, user.getPassword());
		}
	}

	/**
	 * Get a naming context provider instance to use.
	 * 
	 * @return Not null.
	 */
	protected NamingContextProvider getNamingContextProvider() {
		if (null == this.namingContextProvider) {
			this.setNamingContextProvider(new NamingContextProviderImpl());
		}
		return this.namingContextProvider;
	}

	/**
	 * Set the naming context provider to use.
	 * 
	 * @param namingContextProvider
	 *            Not null.
	 */
	void setNamingContextProvider(final NamingContextProvider namingContextProvider) {
		this.namingContextProvider = namingContextProvider;
	}
}
