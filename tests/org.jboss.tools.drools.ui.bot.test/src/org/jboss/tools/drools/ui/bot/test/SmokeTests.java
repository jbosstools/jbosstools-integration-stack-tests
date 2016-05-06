package org.jboss.tools.drools.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.smoke.DroolsProjectTest;
import org.jboss.tools.drools.ui.bot.test.smoke.KieNavigatorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.PerspectiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	PerspectiveTest.class,
	KieNavigatorTest.class,
	DroolsProjectTest.class
})
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
