package org.jboss.tools.fuse.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.RegressionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs regression tests - verifying resolved issues
 * 
 * @author tsedmik
 */
@SuiteClasses({
	RegressionTest.class
})
@RunWith(RedDeerSuite.class)
public class RegressionTests extends TestSuite {

}
