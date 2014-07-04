package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Test initialization and environment configuration
 * 
 * @author tsedmik
 */
public class FuseSuite extends RedDeerSuite {

	public FuseSuite(Class<?> clazz, RunnerBuilder builder) throws InitializationError {
		super(clazz, builder);
	}
}
