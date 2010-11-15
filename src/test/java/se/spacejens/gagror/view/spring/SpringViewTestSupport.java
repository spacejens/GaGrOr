package se.spacejens.gagror.view.spring;

import org.junit.Before;

import se.spacejens.gagror.TestSupport;

/**
 * Superclass for unit tests of Spring controllers.
 * 
 * @author spacejens
 * 
 * @param <C>
 *            Type of controller to test.
 */
public abstract class SpringViewTestSupport<C extends SpringViewSupport>
		extends TestSupport {

	/** Controller instance used for tests. */
	private C instance;

	/**
	 * Get the controller instance used for this test case.
	 * 
	 * @return The controller instance.
	 */
	protected C getInstance() {
		return this.instance;
	}

	/**
	 * Setup data needed for each test case.
	 */
	@Before
	public void setup() {
		this.instance = this.createInstance();
	}

	/**
	 * Create a new instance of the controller type to test.
	 * 
	 * @return A newly created instance.
	 */
	protected abstract C createInstance();
}
