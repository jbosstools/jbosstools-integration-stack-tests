package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.DebuggerBreakpointTest;
import org.jboss.tools.switchyard.ui.bot.test.DebuggerDebuggingTest;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerJavaWSDLTest;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerProjectCapabilitiesTest;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerProjectCreationTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorBindingsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorDomainSettingsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorImplementationsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorThrottlingTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorTransformersTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorValidatorsTest;
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
	ProjectExplorerProjectCreationTest.class,
	ProjectExplorerProjectCapabilitiesTest.class,
	ProjectExplorerJavaWSDLTest.class,
	SwitchYardEditorImplementationsTest.class,
	SwitchYardEditorDomainSettingsTest.class,
	SwitchYardEditorBindingsTest.class,
	SwitchYardEditorThrottlingTest.class,
	SwitchYardEditorTransformersTest.class,
	SwitchYardEditorValidatorsTest.class,
	SwitchYardIntegrationBeanTest.class,
	SwitchYardIntegrationBPELTest.class,
	SwitchYardIntegrationBPMN2Test.class,
	SwitchYardIntegrationCamelTest.class,
	SwitchYardIntegrationDroolsTest.class,
	DebuggerBreakpointTest.class,
	DebuggerDebuggingTest.class})
@RunWith(RedDeerSuite.class)
public class WithoutRuntimeTests extends TestSuite {

}
