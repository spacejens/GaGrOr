package se.spacejens.gagror.model.user;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Assert;
import org.junit.Test;

import se.spacejens.gagror.model.EntityTestSupport;

/**
 * Unit tests for {@link UserEntity}.
 * 
 * @author spacejens
 */
public class UserTest extends EntityTestSupport {

	/**
	 * Verify correct behaviour for valid contents.
	 */
	@Test
	public void testOK() {
		for (int uLength = UserEntity.USERNAME_MIN_LENGTH; uLength <= UserEntity.USERNAME_MAX_LENGTH; uLength++) {
			for (int pLength = UserEntity.PASSWORD_ENCRYPTED_MIN_LENGTH; pLength <= UserEntity.PASSWORD_ENCRYPTED_MAX_LENGTH; pLength++) {
				this.log.debug("Validating user with username length {} and password length {}", uLength, pLength);
				final UserEntityImpl user = new UserEntityImpl();
				user.setUsername(this.buildString(uLength));
				user.setPassword(this.buildString(pLength));
				final Set<ConstraintViolation<UserEntityImpl>> result = this.getValidator().validate(user);
				Assert.assertEquals("Unexpected number of violations", 0, result.size());
			}
		}
	}
}
