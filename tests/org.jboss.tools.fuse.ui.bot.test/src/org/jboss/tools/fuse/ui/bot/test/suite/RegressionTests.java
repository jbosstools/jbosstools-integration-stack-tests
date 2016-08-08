package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.RegressionFuseTest;
import org.jboss.tools.fuse.ui.bot.test.RegressionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Runs regression tests - verifying resolved issues
 * 
 * @author tsedmik
 */
@SuiteClasses({ RegressionTest.class, RegressionFuseTest.class })
@RunWith(RedDeerSuite.class)
public class RegressionTests extends TestSuite {

}
