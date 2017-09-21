package org.jboss.tools.drools.ui.bot.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.smoke.KieNavigatorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.PerspectiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

@SuiteClasses({
	PerspectiveTest.class,
	KieNavigatorTest.class
})
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
