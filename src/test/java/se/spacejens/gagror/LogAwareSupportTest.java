package se.spacejens.gagror;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for {@link LogAwareSupport}.
 * 
 * @author spacejens
 */
public class LogAwareSupportTest extends TestSupport {

	/** Instance to test. */
	private LogAwareSupport instance;

	/**
	 * Each test method receives a fresh instance to test.
	 */
	@Before
	public void setup() {
		this.instance = new LogAwareSupportSub();
	}

	/**
	 * Verify that the logger instance is as expected.
	 */
	@Test
	public void testLoggerInstance() {
		Assert.assertNotNull("No logger instance", this.instance.getLog());
		final Logger other = LoggerFactory.getLogger(LogAwareSupportSub.class);
		Assert.assertTrue("Should have received same logger instance",
				this.instance.getLog() == other);
	}

	/**
	 * Subclass needed to test abstract class.
	 * 
	 * @author spacejens
	 */
	private class LogAwareSupportSub extends LogAwareSupport {
	}
}
