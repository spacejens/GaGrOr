package se.spacejens.gagror.controller.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import se.spacejens.gagror.TestSupport;
import se.spacejens.gagror.model.JpaContext;
import se.spacejens.gagror.model.user.User;

/**
 * Unit test for {@link EncryptionHelperImpl}.
 * 
 * @author spacejens
 */
public class EncrypterTest extends TestSupport {

	/** Test object instance, freshly created for each test case. */
	private EncryptionHelperImpl instance;

	/**
	 * Create a test object instance.
	 */
	@Before
	public void createInstance() {
		final JpaContext jpa = Mockito.mock(JpaContext.class);
		this.instance = new EncryptionHelperImpl(jpa);
	}

	/**
	 * Verify that the used encryption algorithm exists.
	 */
	@Test
	public void algorithmExists() {
		this.instance.getMessageDigest();
	}

	/**
	 * Verify that the used character encoding exists.
	 */
	@Test
	public void characterEncodingExists() {
		this.instance.getBytes("Whatever");
	}

	/**
	 * Encrypt the same password for two different users, verify that they get
	 * the expected (different) results. This verifies both that the encryption
	 * doesn't change at some point, and that security is good.
	 */
	@Test
	public void encryptPassword() {
		final String user1 = "FirstUser";
		final String user2 = "SecondUser";
		final String password = "Not so secret";
		final String expected1 = "SoQRRozH0LU88TlXQ961YUVywSU=";
		final String expected2 = "VRmNPvMT+yshpp8O+MSDDCjtbV4=";
		final String result1 = this.instance.encryptPassword(user1, password);
		final String result2 = this.instance.encryptPassword(user2, password);
		Assert.assertEquals("Unexpected encryption result for first user", expected1, result1);
		Assert.assertEquals("Unexpected encryption result for second user", expected2, result2);
	}

	/**
	 * Verify that the password length is within specified limits after
	 * encryption.
	 */
	@Test
	public void encryptPasswordLength() {
		// Create a long enough (and then some) string to work with
		final StringBuffer textBuffer = new StringBuffer();
		for (int i = 0; i < User.USERNAME_MAX_LENGTH + User.PASSWORD_MAX_LENGTH; i++) {
			textBuffer.append(Integer.toString(i));
		}
		final String text = textBuffer.toString();
		this.log.debug("Assembled dummy string: " + text);
		// Test all possible length combinations
		for (int userLength = User.USERNAME_MIN_LENGTH; userLength <= User.USERNAME_MAX_LENGTH; userLength++) {
			for (int passwordLength = User.PASSWORD_MIN_LENGTH; passwordLength <= User.PASSWORD_MAX_LENGTH; passwordLength++) {
				final String encrypted = this.instance.encryptPassword(text.substring(0, userLength), text.substring(0, passwordLength));
				Assert.assertTrue("Encrypted password less than minimum length", encrypted.length() >= User.PASSWORD_ENCRYPTED_MIN_LENGTH);
				Assert.assertTrue("Encrypted password greater than maximum length", encrypted.length() <= User.PASSWORD_ENCRYPTED_MAX_LENGTH);
			}
		}
	}
}
