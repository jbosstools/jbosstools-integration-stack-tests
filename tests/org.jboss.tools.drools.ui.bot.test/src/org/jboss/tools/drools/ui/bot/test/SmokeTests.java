package org.jboss.tools.drools.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.functional.PerspectiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ PerspectiveTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
