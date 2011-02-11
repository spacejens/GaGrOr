package se.spacejens.gagror.model.user;

/**
 * Implementation of reference to the currently logged in user.
 * 
 * @author spacejens
 */
public class UserLoggedInDTOImpl extends UserReferenceDTOImpl implements UserLoggedInDTO {

	/** Encrypted password of the user. */
	private final String password;

	/**
	 * Create a reference to the currently logged in user.
	 * 
	 * @param entity
	 *            The currently logged in user.
	 */
	public UserLoggedInDTOImpl(final UserEntity entity) {
		super(entity);
		this.password = entity.getPassword();
	}

	@Override
	public String getPassword() {
		return this.password;
	}
}
