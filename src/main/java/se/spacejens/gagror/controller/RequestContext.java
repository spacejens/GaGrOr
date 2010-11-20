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
}
