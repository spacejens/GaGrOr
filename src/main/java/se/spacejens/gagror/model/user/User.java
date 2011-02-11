package se.spacejens.gagror.model.user;

/**
 * Interface defining operations shared between user entity and DTOs (all).
 * 
 * @author spacejens
 */
public interface User {

	/**
	 * Get the username.
	 * 
	 * @return Not null.
	 */
	public String getUsername();
}
