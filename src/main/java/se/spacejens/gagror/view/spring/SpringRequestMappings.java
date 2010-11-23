package se.spacejens.gagror.view.spring;

import se.spacejens.gagror.GagrorImplementationException;
import se.spacejens.gagror.view.spring.dashboard.DashboardPages;

/**
 * Request mapping constants for all Spring controllers.
 * 
 * @author spacejens
 */
public class SpringRequestMappings {

	/** @see PublicPages */
	public static final String PUBLIC = "";

	/** @see PublicPages#index(javax.servlet.http.HttpServletRequest) */
	public static final String PUBLIC_INDEX = "/index.html";

	/**
	 * @see PublicPages#registerFormGet(javax.servlet.http.HttpServletRequest)
	 * @see PublicPages#registerFormPost(javax.servlet.http.HttpServletRequest,
	 *      UserRegistrationForm, org.springframework.validation.BindingResult)
	 */
	public static final String PUBLIC_REGISTER = "/register.html";

	/**
	 * @see PublicPages#login(javax.servlet.http.HttpServletRequest)
	 * @see PublicPages#postLoginForm(javax.servlet.http.HttpServletRequest,
	 *      LoginForm, org.springframework.validation.BindingResult)
	 */
	public static final String PUBLIC_LOGIN = "/login.html";

	/** @see PublicPages#notLoggedIn(javax.servlet.http.HttpServletRequest) */
	public static final String PUBLIC_NOTLOGGEDIN = "/not_logged_in.html";

	/** @see PublicPages#logout() */
	public static final String PUBLIC_LOGGEDOUT = "/logout.html";

	/** @see DashboardPages */
	public static final String DASHBOARD = "/dashboard";

	/** @see DashboardPages#index() */
	public static final String DASHBOARD_INDEX = "/index.html";

	/**
	 * This constructor is never used, declared to enforce static use of
	 * constants only.
	 */
	private SpringRequestMappings() {
		throw new GagrorImplementationException();
	}
}
