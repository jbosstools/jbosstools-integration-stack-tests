package org.jboss.tools.runtime.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.ui.bot.test.ServerPresentTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ServerPresentTest.class})
@RunWith(RedDeerSuite.class)
public class SmokeTests {

}
