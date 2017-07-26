package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationDeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorServerTest;
import org.jboss.tools.fuse.ui.bot.test.QuickStartsTest;
import org.jboss.tools.fuse.ui.bot.test.ServerJRETest;
import org.jboss.tools.fuse.ui.bot.test.ServerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Runs tests that need a Fuse server instance on Fuse Tooling
 * 
 * @author tsedmik
 */
@SuiteClasses({
	DataTransformationDeploymentTest.class,
	DeploymentTest.class,
	JMXNavigatorServerTest.class,
	QuickStartsTest.class,
	ServerTest.class,
	ServerJRETest.class })
@RunWith(RedDeerSuite.class)
public class ServerTests extends TestSuite {

}
