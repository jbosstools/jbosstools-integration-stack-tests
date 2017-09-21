package org.jboss.tools.drools.ui.bot.test;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.smoke.ConvertProjectTest;
import org.jboss.tools.drools.ui.bot.test.smoke.DroolsProjectTest;
import org.jboss.tools.drools.ui.bot.test.smoke.KieNavigatorTest;
import org.jboss.tools.drools.ui.bot.test.smoke.PerspectiveTest;
import org.jboss.tools.drools.ui.bot.test.smoke.NewResourcesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	PerspectiveTest.class,
	KieNavigatorTest.class,
	DroolsProjectTest.class,
	NewResourcesTest.class,
	ConvertProjectTest.class
})
@RunWith(RedDeerSuite.class)
public class SmokeRuntimeTests extends TestSuite {

}
