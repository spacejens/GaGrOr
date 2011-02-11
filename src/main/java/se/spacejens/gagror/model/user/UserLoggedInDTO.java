package se.spacejens.gagror.model.user;

/**
 * DTO for the currently logged in user, containing information needed for
 * storage in the session to keep the user logged in.
 * 
 * @author spacejens
 */
public interface UserLoggedInDTO extends UserReferenceDTO, Password {
}
