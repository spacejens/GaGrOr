package se.spacejens.gagror;

/**
 * Constants defining various security constraints of the system.
 * 
 * @author spacejens
 */
public interface SecurityConstants {

	/** Minimum length of valid username. */
	public static final int USERNAME_MIN_LENGTH = 2;
	/** Maximum length of valid username. */
	public static final int USERNAME_MAX_LENGTH = 16;
	/** Minimum length of password. */
	public static final int PASSWORD_MIN_LENGTH = 8;
	/** Maximum length of password. */
	public static final int PASSWORD_MAX_LENGTH = 32;
	/** Minimum length of encrypted password. */
	public static final int PASSWORD_ENCRYPTED_MIN_LENGTH = 28;
	/** Maximum length of encrypted password. */
	public static final int PASSWORD_ENCRYPTED_MAX_LENGTH = 28;

	/** One-way encryption hash to use. */
	public static final String ALGORITHM = "SHA";
	/** Character encoding to use. */
	public static final String ENCODING = "UTF-8";
	/** Added between username and password when joining them for encryption. */
	public static final String USERNAME_PASSWORD_JOINING = " ";
}
