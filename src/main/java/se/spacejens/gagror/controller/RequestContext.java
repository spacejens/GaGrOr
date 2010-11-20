package se.spacejens.gagror.controller;

/**
 * Interface specifying the information that should be provided when making a
 * request to the controller layer.
 * 
 * @author spacejens
 */
public interface RequestContext {

	/**
	 * Get the username used for login in this session.
	 * 
	 * @return Null if not present.
	 */
	public String getUsername();

	/**
	 * Get the encrypted password used for login in this session.
	 * 
	 * @return Null if not present.
	 */
	public String getPassword();

	/**
	 * Get the web application context path of the request.
	 * 
	 * @return Not null.
	 */
	public String getContextPath();

	/**
	 * Get the servlet path of the request.
	 * 
	 * @return Not null.
	 */
	public String getServletPath();

	/**
	 * Check if the context contains username, password etc.
	 * 
	 * @return true if any such information is present.
	 */
	public boolean isContainingLoginInformation();
}
