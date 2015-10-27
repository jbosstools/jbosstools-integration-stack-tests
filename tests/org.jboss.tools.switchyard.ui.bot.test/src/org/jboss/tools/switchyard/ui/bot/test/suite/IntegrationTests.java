package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardIntegrationBPELTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardIntegrationBPMN2Test;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardIntegrationBeanTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardIntegrationCamelTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardIntegrationDroolsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
	SwitchYardIntegrationBeanTest.class,
	SwitchYardIntegrationBPELTest.class,
	SwitchYardIntegrationBPMN2Test.class,
	SwitchYardIntegrationCamelTest.class,
	SwitchYardIntegrationDroolsTest.class,
})
@RunWith(RedDeerSuite.class)
public class IntegrationTests extends TestSuite {

}
