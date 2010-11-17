package se.spacejens.gagror.model;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.BeforeClass;

import se.spacejens.gagror.TestSupport;

/**
 * Definition of functionality common to all entity tests.
 * 
 * @author spacejens
 */
public abstract class EntityTestSupport extends TestSupport {

	/** Validator instance to use. */
	private static Validator validator = null;

	/**
	 * Create validator instance shared by all test cases.
	 */
	@BeforeClass
	public static void createValidator() {
		EntityTestSupport.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/**
	 * Get validator instance.
	 * 
	 * @return Not null.
	 */
	protected Validator getValidator() {
		return EntityTestSupport.validator;
	}
}
