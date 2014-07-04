package org.jboss.tools.fuse.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.fuse.ui.bot.test.CamelEditorTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.FabricExplorerTest;
import org.jboss.tools.fuse.ui.bot.test.FabricInCloudTest;
import org.jboss.tools.fuse.ui.bot.test.FuseProjectTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorTest;
import org.jboss.tools.fuse.ui.bot.test.ProjectLocalRunTest;
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
	FabricExplorerTest.class,
	DeploymentTest.class,
	FabricInCloudTest.class
})
@RunWith(FuseSuite.class)
public class AllTests extends TestSuite {

}
