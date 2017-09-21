package org.jboss.tools.runtime.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.ui.bot.test.RuntimeTest;
import org.jboss.tools.runtime.ui.bot.test.ServerPresentTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ ServerPresentTest.class, RuntimeTest.class })
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
