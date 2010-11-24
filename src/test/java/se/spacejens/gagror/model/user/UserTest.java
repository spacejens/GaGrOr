package se.spacejens.gagror.model.user;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Assert;
import org.junit.Test;

import se.spacejens.gagror.model.EntityTestSupport;

/**
 * Unit tests for {@link User}.
 * 
 * @author spacejens
 */
public class UserTest extends EntityTestSupport {

	/**
	 * Verify correct behaviour for valid contents.
	 */
	@Test
	public void testOK() {
		for (int uLength = User.USERNAME_MIN_LENGTH; uLength <= User.USERNAME_MAX_LENGTH; uLength++) {
			for (int pLength = User.PASSWORD_ENCRYPTED_MIN_LENGTH; pLength <= User.PASSWORD_ENCRYPTED_MAX_LENGTH; pLength++) {
				this.log.debug("Validating user with username length {} and password length {}", uLength, pLength);
				final UserImpl user = new UserImpl();
				user.setUsername(this.buildString(uLength));
				user.setPassword(this.buildString(pLength));
				final Set<ConstraintViolation<UserImpl>> result = this.getValidator().validate(user);
				Assert.assertEquals("Unexpected number of violations", 0, result.size());
			}
		}
	}
}
