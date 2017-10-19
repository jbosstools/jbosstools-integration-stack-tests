package org.jboss.tools.switchyard.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerJavaWSDLTest;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerProjectCapabilitiesTest;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerProjectCreationTest;
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
	ProjectExplorerProjectCreationTest.class,
	ProjectExplorerProjectCapabilitiesTest.class,
	ProjectExplorerJavaWSDLTest.class })
@RunWith(RedDeerSuite.class)
public class ProjectExplorerTests extends TestSuite {

}
