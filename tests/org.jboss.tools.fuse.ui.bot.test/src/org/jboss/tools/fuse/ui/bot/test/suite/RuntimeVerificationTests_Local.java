package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.QuickStartsTest;
import org.jboss.tools.fuse.ui.bot.test.RegressionFuseTest;
import org.jboss.tools.fuse.ui.bot.test.RegressionTest;
import org.jboss.tools.fuse.ui.bot.test.ServerJRETest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Runs verification tests for a JBoss Fuse runtime.
 * 
 * @author tsedmik
 */
@SuiteClasses({
	RegressionFuseTest.class,
	RegressionTest.class,
	QuickStartsTest.class,
	ServerJRETest.class })
@RunWith(RedDeerSuite.class)
public class RuntimeVerificationTests_Local extends TestSuite {

}
