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

	/** The web application context path of the request. */
	private final String contextPath;

	/** The servlet path of the request. */
	private final String servletPath;

	/**
	 * Create instance from information stored in servlet request.
	 * 
	 * @param request
	 *            Not null.
	 */
	public WebRequestContext(final HttpServletRequest request) {
		Object sessionUsername = request.getSession().getAttribute(WebSessionAttributes.USERNAME);
		if (null == sessionUsername) {
			this.username = null;
		} else {
			this.username = sessionUsername.toString();
		}
		Object sessionPassword = request.getSession().getAttribute(WebSessionAttributes.PASSWORD);
		if (null == sessionPassword) {
			this.password = null;
		} else {
			this.password = sessionPassword.toString();
		}
		this.contextPath = request.getContextPath();
		this.servletPath = request.getServletPath();
		this.getLog().debug("Created web request context from HTTP servlet request, username={}, servlet path={}", this.username, this.servletPath);
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getContextPath() {
		return this.contextPath;
	}

	@Override
	public String getServletPath() {
		return this.servletPath;
	}

	@Override
	public boolean isContainingLoginInformation() {
		return this.getUsername() != null || this.getPassword() != null;
	}
}
