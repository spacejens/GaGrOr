package se.spacejens.gagror.controller.ejb.core;

import se.spacejens.gagror.controller.RequestResult;
import se.spacejens.gagror.model.user.UserReferenceDTO;

/**
 * Result of a request to log out.
 * 
 * @author spacejens
 */
public interface LogoutRequestResult extends RequestResult {

	/**
	 * The logged out user.
	 * 
	 * @return Null if no user was logged in.
	 */
	public UserReferenceDTO getLoggedOutUser();
}
