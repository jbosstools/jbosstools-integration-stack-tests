package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.DomainSettingsTest;
import org.jboss.tools.switchyard.ui.bot.test.HelloWorldTest;
import org.jboss.tools.switchyard.ui.bot.test.ImplementationsPropertiesTest;
import org.jboss.tools.switchyard.ui.bot.test.ImplementationsTest;
import org.jboss.tools.switchyard.ui.bot.test.ValidatorsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
//	HelloWorldTest.class,
	ImplementationsTest.class,
	DomainSettingsTest.class,
	ImplementationsPropertiesTest.class,
//	ValidatorsTest.class
})
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
