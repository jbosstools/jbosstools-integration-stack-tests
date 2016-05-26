package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.ProjectLocalRunTest;
import org.jboss.tools.fuse.ui.bot.test.SimpleTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Runs smoke tests on Fuse Tooling
 * 
 * @author tsedmik
 */
@SuiteClasses({ ProjectLocalRunTest.class, SimpleTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}