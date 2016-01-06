package org.jboss.tools.esb.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.esb.ui.bot.test.NewESBFileTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ NewESBFileTest.class, })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
