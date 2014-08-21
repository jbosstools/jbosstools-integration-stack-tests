package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.CamelQuickstartsTest;
import org.jboss.tools.switchyard.ui.bot.test.DTGovQuickstartsTest;
import org.jboss.tools.switchyard.ui.bot.test.DemoQuickstartsTest;
import org.jboss.tools.switchyard.ui.bot.test.RTGovQuickstartsTest;
import org.jboss.tools.switchyard.ui.bot.test.SRampQuickstartsTest;
import org.jboss.tools.switchyard.ui.bot.test.SwitchYardQuickstartsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
	SwitchYardQuickstartsTest.class,
	DemoQuickstartsTest.class,
	DTGovQuickstartsTest.class,
	RTGovQuickstartsTest.class,
	SRampQuickstartsTest.class,
	CamelQuickstartsTest.class
})
@RunWith(RedDeerSuite.class)
public class QuickstartTests extends TestSuite {

}
