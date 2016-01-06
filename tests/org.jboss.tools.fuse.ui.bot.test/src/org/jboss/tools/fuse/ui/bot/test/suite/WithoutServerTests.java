package org.jboss.tools.fuse.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.CamelEditorTest;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationTest;
import org.jboss.tools.fuse.ui.bot.test.DebuggerTest;
import org.jboss.tools.fuse.ui.bot.test.DownloadServerTest;
import org.jboss.tools.fuse.ui.bot.test.FabricInCloudTest;
import org.jboss.tools.fuse.ui.bot.test.FuseProjectTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorTest;
import org.jboss.tools.fuse.ui.bot.test.ProjectLocalRunTest;
import org.jboss.tools.fuse.ui.bot.test.RouteManipulationTest;
import org.jboss.tools.fuse.ui.bot.test.SmokeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs tests that do not need a Fuse server instance on Fuse Tooling
 * 
 * @author tsedmik
 */
@SuiteClasses({
	CamelEditorTest.class,
	DataTransformationTest.class,
	DebuggerTest.class,
	DownloadServerTest.class,
	FabricInCloudTest.class,
	FuseProjectTest.class,
	JMXNavigatorTest.class,
	ProjectLocalRunTest.class,
	RouteManipulationTest.class,
	SmokeTest.class })
@RunWith(RedDeerSuite.class)
public class WithoutServerTests extends TestSuite {

}
