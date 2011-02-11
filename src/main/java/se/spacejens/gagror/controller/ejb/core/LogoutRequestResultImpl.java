package se.spacejens.gagror.controller.ejb.core;

import se.spacejens.gagror.controller.RequestResultImpl;
import se.spacejens.gagror.model.user.UserEntity;
import se.spacejens.gagror.model.user.UserReferenceDTO;
import se.spacejens.gagror.model.user.UserReferenceDTOImpl;

/**
 * Implementation of a logout request result.
 * 
 * @author spacejens
 */
public class LogoutRequestResultImpl extends RequestResultImpl implements LogoutRequestResult {

	/** The logged out user. */
	private final UserReferenceDTO loggedOut;

	/**
	 * Create a result when no user was logged in.
	 */
	public LogoutRequestResultImpl() {
		this((UserReferenceDTO) null);
	}

	/**
	 * Create a result for the logged out user.
	 * 
	 * @param loggedOut
	 *            Null if no user was logged in.
	 */
	public LogoutRequestResultImpl(final UserReferenceDTO loggedOut) {
		super();
		this.loggedOut = loggedOut;
	}

	/**
	 * Create a result for the logged out user.
	 * 
	 * @param loggedOut
	 *            Not null.
	 */
	public LogoutRequestResultImpl(final UserEntity loggedOut) {
		this(new UserReferenceDTOImpl(loggedOut));
	}

	@Override
	public UserReferenceDTO getLoggedOutUser() {
		return this.loggedOut;
	}
}
