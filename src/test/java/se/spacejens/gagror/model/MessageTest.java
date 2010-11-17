package se.spacejens.gagror.model;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link Message}.
 * 
 * @author spacejens
 */
public class MessageTest extends EntityTestSupport {

	/**
	 * Verify correct validation behavior when text is null.
	 */
	@Test
	public void textNotNull() {
		final Message mess = new Message(null);
		final Set<ConstraintViolation<Message>> result = this.getValidator().validate(mess);
		Assert.assertEquals("Unexpected number of violations", 1, result.size());
		Assert.assertEquals("Unexpected violation", "may not be null", result.iterator().next().getMessage());
	}

	/**
	 * Verify correct validation behavior when text is empty string.
	 */
	@Test
	public void textEmptyString() {
		final Message mess = new Message("");
		final Set<ConstraintViolation<Message>> result = this.getValidator().validate(mess);
		Assert.assertEquals("Unexpected number of violations", 1, result.size());
		Assert.assertEquals("Unexpected violation", "size must be between 1 and 255", result.iterator().next().getMessage());
	}

	/**
	 * Verify correct validation behavior when text is non-empty.
	 */
	@Test
	public void textOK() {
		final Message mess = new Message("This is OK");
		final Set<ConstraintViolation<Message>> result = this.getValidator().validate(mess);
		Assert.assertEquals("Unexpected number of violations", 0, result.size());
	}
}
