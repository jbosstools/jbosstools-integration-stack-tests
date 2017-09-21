package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorDomainSettingsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorImplementationsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorValidatorsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardIntegrationBeanTest;
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
	SwitchYardEditorImplementationsTest.class,
	SwitchYardEditorDomainSettingsTest.class,
	SwitchYardEditorValidatorsTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
