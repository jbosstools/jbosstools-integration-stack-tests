package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.CamelEditorTest;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationDeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationTest;
import org.jboss.tools.fuse.ui.bot.test.DebuggerTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentEAPTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.DownloadServerTest;
import org.jboss.tools.fuse.ui.bot.test.FeaturesTest;
import org.jboss.tools.fuse.ui.bot.test.FuseProjectTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorServerTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorTest;
import org.jboss.tools.fuse.ui.bot.test.LicenseTest;
import org.jboss.tools.fuse.ui.bot.test.ProjectLocalRunTest;
import org.jboss.tools.fuse.ui.bot.test.QuickStartsTest;
import org.jboss.tools.fuse.ui.bot.test.RouteManipulationTest;
import org.jboss.tools.fuse.ui.bot.test.ServerJRETest;
import org.jboss.tools.fuse.ui.bot.test.ServerTest;
import org.jboss.tools.fuse.ui.bot.test.SmokeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Runs all tests
 * 
 * @author tsedmik
 */
@SuiteClasses({
	CamelEditorTest.class,
	DataTransformationTest.class,
	DataTransformationDeploymentTest.class,
	DebuggerTest.class,
	DeploymentEAPTest.class,
	DeploymentTest.class,
	DownloadServerTest.class,
	FeaturesTest.class,
	FuseProjectTest.class,
	JMXNavigatorServerTest.class,
	JMXNavigatorTest.class,
	LicenseTest.class,
	ProjectLocalRunTest.class,
	QuickStartsTest.class,
	RouteManipulationTest.class,
	ServerTest.class,
	SmokeTest.class,
	ServerJRETest.class
})
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
