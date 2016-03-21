package org.jboss.tools.switchyard.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorBindingsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorDomainSettingsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorImplementationsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorThrottlingTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorTransformersTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardEditorValidatorsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
	SwitchYardEditorBindingsTest.class,
	SwitchYardEditorDomainSettingsTest.class,
	SwitchYardEditorImplementationsTest.class,
	SwitchYardEditorThrottlingTest.class,
	SwitchYardEditorTransformersTest.class,
	SwitchYardEditorValidatorsTest.class })
@RunWith(RedDeerSuite.class)
public class EditorTests extends TestSuite {

}
