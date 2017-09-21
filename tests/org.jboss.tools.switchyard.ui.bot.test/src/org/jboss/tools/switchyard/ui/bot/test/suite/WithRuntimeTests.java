package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.ProjectExplorerProjectCreationTest;
import org.jboss.tools.switchyard.ui.bot.test.ServerManagingDeploymentTest;
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
	ServerManagingDeploymentTest.class })
@RunWith(RedDeerSuite.class)
public class WithRuntimeTests extends TestSuite {

}
