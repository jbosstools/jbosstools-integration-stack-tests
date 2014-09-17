package org.jboss.tools.fuse.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.CamelEditorTest;
import org.jboss.tools.fuse.ui.bot.test.DebuggerTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.FuseProjectTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorTest;
import org.jboss.tools.fuse.ui.bot.test.ProjectLocalRunTest;
import org.jboss.tools.fuse.ui.bot.test.RegressionTest;
import org.jboss.tools.fuse.ui.bot.test.RouteManipulationTest;
import org.jboss.tools.fuse.ui.bot.test.ServerTest;
import org.jboss.tools.fuse.ui.bot.test.SmokeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all tests
 * 
 * @author tsedmik
 */
@SuiteClasses({
	SmokeTest.class,
	ProjectLocalRunTest.class,
	JMXNavigatorTest.class,
	CamelEditorTest.class,
	FuseProjectTest.class,
	ServerTest.class,
	DeploymentTest.class,
	DebuggerTest.class,
	RegressionTest.class,
	RouteManipulationTest.class
})
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
