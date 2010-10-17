package se.spacejens.gagror;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest extends AbstractTest {
	private App instance;

	@Before
	public void setup() {
		this.log.debug("Creating test instance");
		this.instance = new App();
	}

	@Test
	public void testApp() {
		this.log.info("Testing");
		Assert.assertNotNull("Missing message", this.instance.getMessage());
		Assert.assertTrue("Empty message",
				this.instance.getMessage().length() > 0);
		this.log.debug("Test done");
	}
}
