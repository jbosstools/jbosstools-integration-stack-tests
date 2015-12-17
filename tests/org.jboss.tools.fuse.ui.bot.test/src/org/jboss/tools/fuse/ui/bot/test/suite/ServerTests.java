package org.jboss.tools.fuse.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.DeploymentEAPTest;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationDeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.Fabric8Test;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorServerTest;
import org.jboss.tools.fuse.ui.bot.test.QuickStartsTest;
import org.jboss.tools.fuse.ui.bot.test.ServerJRETest;
import org.jboss.tools.fuse.ui.bot.test.ServerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs tests that need a Fuse server instance on Fuse Tooling
 * 
 * @author tsedmik
 */
@SuiteClasses({
	DeploymentEAPTest.class,
	DataTransformationDeploymentTest.class,
	DeploymentTest.class,
	Fabric8Test.class,
	JMXNavigatorServerTest.class,
	QuickStartsTest.class,
	ServerTest.class,
	ServerJRETest.class
})
@RunWith(RedDeerSuite.class)
public class ServerTests extends TestSuite {

}
