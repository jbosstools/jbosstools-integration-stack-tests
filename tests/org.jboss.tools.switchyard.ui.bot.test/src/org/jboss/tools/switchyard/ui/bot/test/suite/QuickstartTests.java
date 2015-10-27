package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.QuickstartsCamelTest;
import org.jboss.tools.switchyard.ui.bot.test.QuickstartsDTGovTest;
import org.jboss.tools.switchyard.ui.bot.test.QuickstartsDemoTest;
import org.jboss.tools.switchyard.ui.bot.test.QuickstartsRTGovTest;
import org.jboss.tools.switchyard.ui.bot.test.QuickstartsSRampTest;
import org.jboss.tools.switchyard.ui.bot.test.QuickstartsSwitchYardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
	QuickstartsSwitchYardTest.class,
	QuickstartsDemoTest.class,
	QuickstartsDTGovTest.class,
	QuickstartsRTGovTest.class,
	QuickstartsSRampTest.class,
	QuickstartsCamelTest.class
})
@RunWith(RedDeerSuite.class)
public class QuickstartTests extends TestSuite {

}
