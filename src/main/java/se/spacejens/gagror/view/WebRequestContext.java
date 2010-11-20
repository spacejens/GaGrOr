package se.spacejens.gagror.view;

import javax.servlet.http.HttpServletRequest;

import se.spacejens.gagror.LogAwareSupport;
import se.spacejens.gagror.controller.RequestContext;

/**
 * Request context for controller layer requests originating from the view
 * layer.
 * 
 * @author spacejens
 */
class WebRequestContext extends LogAwareSupport implements RequestContext {

	/** The username used for login in this session. */
	private final String username;

	/** The password used for login in this session. */
	private final String password;

	/**
	 * Create instance from information stored in servlet request.
	 * 
	 * @param request
	 *            Not null.
	 */
	public WebRequestContext(final HttpServletRequest request) {
		Object sessionUsername = request.getSession().getAttribute("username");
		if (null == sessionUsername) {
			this.username = null;
		} else {
			this.username = sessionUsername.toString();
		}
		Object sessionPassword = request.getSession().getAttribute("password");
		if (null == sessionPassword) {
			this.password = null;
		} else {
			this.password = sessionPassword.toString();
		}
		this.getLog().debug("Created web request context from HTTP servlet request, username={}", this.username);
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}
}
