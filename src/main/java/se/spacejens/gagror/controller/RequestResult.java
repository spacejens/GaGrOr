package se.spacejens.gagror.controller;

import se.spacejens.gagror.model.user.UserLoggedInDTO;
import se.spacejens.gagror.model.user.UserReferenceDTO;

/**
 * Result of a call to a controller service operation. Subclasses define
 * additional content specific to the operation in question. Failing operations
 * normally throw exceptions instead.
 * 
 * @author spacejens
 */
public interface RequestResult {

	/**
	 * Get the currently logged in user.
	 * 
	 * @return null if no user is logged in.
	 */
	public UserLoggedInDTO getLoggedInUser();

	/**
	 * Get the currently acting user. May be different from the logged in user
	 * if currently acting as someone else.
	 * 
	 * @return null if no user is logged in.
	 */
	public UserReferenceDTO getActingUser();

	/**
	 * Is the logged in user acting as someone else?
	 * 
	 * @return true if so.
	 */
	public boolean isImpersonating();
}
