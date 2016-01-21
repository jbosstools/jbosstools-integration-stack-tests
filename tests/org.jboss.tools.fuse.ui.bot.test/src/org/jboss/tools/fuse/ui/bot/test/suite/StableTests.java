package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.DeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.DownloadServerTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorServerTest;
import org.jboss.tools.fuse.ui.bot.test.JMXNavigatorTest;
import org.jboss.tools.fuse.ui.bot.test.LicenseTest;
import org.jboss.tools.fuse.ui.bot.test.ProjectLocalRunTest;
import org.jboss.tools.fuse.ui.bot.test.QuickStartsTest;
import org.jboss.tools.fuse.ui.bot.test.ServerTest;
import org.jboss.tools.fuse.ui.bot.test.SmokeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Stable test for JBoss Fuse Tooling
 * 
 * @author tsedmik
 */
@SuiteClasses({
	DeploymentTest.class,
	DownloadServerTest.class,
	JMXNavigatorTest.class,
	JMXNavigatorServerTest.class,
	LicenseTest.class,
	ProjectLocalRunTest.class,
	QuickStartsTest.class,
	ServerTest.class,
	SmokeTest.class })
@RunWith(RedDeerSuite.class)
public class StableTests extends TestSuite {

}
