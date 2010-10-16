package se.spacejens.gagror;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private static Logger log = LoggerFactory.getLogger(AppTest.class);

	private App instance;

	@Before
	public void setup() {
		AppTest.log.debug("Creating test instance");
		this.instance = new App();
	}

	@Test
	public void testApp() {
		AppTest.log.info("Testing");
		Assert.assertNotNull("Missing message", this.instance.getMessage());
		Assert.assertTrue("Empty message",
				this.instance.getMessage().length() > 0);
		AppTest.log.debug("Test done");
	}
}
