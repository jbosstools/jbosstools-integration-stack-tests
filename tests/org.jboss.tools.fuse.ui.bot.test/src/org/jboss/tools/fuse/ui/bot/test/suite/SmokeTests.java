package org.jboss.tools.fuse.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.tools.fuse.ui.bot.test.SmokeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs smoke tests on Fuse Tooling
 * 
 * @author tsedmik
 */
@SuiteClasses(SmokeTest.class)
@RunWith(FuseSuite.class)
public class SmokeTests extends TestSuite {

}