package se.spacejens.gagror.controller;

import se.spacejens.gagror.GagrorImplementationException;
import se.spacejens.gagror.model.user.UserEntity;
import se.spacejens.gagror.model.user.UserLoggedInDTO;
import se.spacejens.gagror.model.user.UserLoggedInDTOImpl;
import se.spacejens.gagror.model.user.UserReferenceDTO;
import se.spacejens.gagror.model.user.UserReferenceDTOImpl;

/**
 * Implementation of the basic request result.
 * 
 * @author spacejens
 */
public class RequestResultImpl implements RequestResult {

	/** The currently logged in user. */
	private final UserLoggedInDTO loggedIn;

	/** The currently acting user. */
	private final UserReferenceDTO acting;

	/**
	 * Create a result where no user is logged in.
	 */
	public RequestResultImpl() {
		this((UserLoggedInDTO) null);
	}

	/**
	 * Create a result where a user is logged in, acting as itself.
	 * 
	 * @param loggedIn
	 *            The logged in user.
	 */
	public RequestResultImpl(final UserLoggedInDTO loggedIn) {
		this(loggedIn, loggedIn);
	}

	/**
	 * Create a result where a user is logged in, acting as itself.
	 * 
	 * @param loggedIn
	 *            The logged in user, not null.
	 */
	public RequestResultImpl(final UserEntity loggedIn) {
		this(new UserLoggedInDTOImpl(loggedIn));
	}

	/**
	 * Create a result where a user is logged in, acting as someone else.
	 * 
	 * @param loggedIn
	 *            The logged in user.
	 * @param acting
	 *            The acting user.
	 */
	public RequestResultImpl(final UserLoggedInDTO loggedIn, final UserReferenceDTO acting) {
		if (null == loggedIn && null != acting) {
			throw new GagrorImplementationException("Cannot act as another user without being logged in");
		}
		if (null != loggedIn && null == acting) {
			throw new GagrorImplementationException("Cannot be logged in without acting as a user (oneself or another)");
		}
		this.loggedIn = loggedIn;
		this.acting = acting;
	}

	/**
	 * Create a result where a user is logged in, acting as someone else.
	 * 
	 * @param loggedIn
	 *            The logged in user, not null.
	 * @param acting
	 *            The acting user, not null.
	 */
	public RequestResultImpl(final UserEntity loggedIn, final UserEntity acting) {
		this(new UserLoggedInDTOImpl(loggedIn), new UserReferenceDTOImpl(acting));
	}

	@Override
	public UserLoggedInDTO getLoggedInUser() {
		return this.loggedIn;
	}

	@Override
	public UserReferenceDTO getActingUser() {
		return this.acting;
	}

	@Override
	public boolean isImpersonating() {
		return this.loggedIn != this.acting;
	}
}
