package org.jboss.tools.runtime.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.ui.bot.test.ServerPresentTest;
import org.jboss.tools.runtime.ui.bot.test.ESBRuntimeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ ServerPresentTest.class, ESBRuntimeTest.class })
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
