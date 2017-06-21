package org.jboss.tools.bpel.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpel.ui.bot.test.ActivityModelingTest;
import org.jboss.tools.bpel.ui.bot.test.AssignActivityTest;
import org.jboss.tools.bpel.ui.bot.test.AssociateRuntimeTest;
import org.jboss.tools.bpel.ui.bot.test.FaultModelingTest;
import org.jboss.tools.bpel.ui.bot.test.SimpleDeployTest;
import org.jboss.tools.bpel.ui.bot.test.WizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	WizardTest.class,
	ActivityModelingTest.class,
	AssignActivityTest.class,
	FaultModelingTest.class,
// JBTIS-325
// ExampleTest.class
// JBTIS-674
// SimpleDeployTest.class
// AssociateRuntimeTest.class
})
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
