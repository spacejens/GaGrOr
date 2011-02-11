package se.spacejens.gagror.model.user;

import se.spacejens.gagror.model.DTOImpl;

/**
 * Implementation of reference to a user.
 * 
 * @author spacejens
 */
public class UserReferenceDTOImpl extends DTOImpl implements UserReferenceDTO {

	/** The username of the referred user. */
	private final String username;

	/**
	 * Create a reference to a provided user entity.
	 * 
	 * @param entity
	 *            The user to refer to.
	 */
	public UserReferenceDTOImpl(final UserEntity entity) {
		super(entity);
		this.username = entity.getUsername();
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String toString() {
		final StringBuffer output = new StringBuffer();
		output.append(this.getUsername());
		output.append("(");
		output.append(super.toString());
		output.append(")");
		return output.toString();
	}
}
