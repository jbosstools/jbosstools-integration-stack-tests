package org.jboss.tools.bpel.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpel.ui.bot.test.ActivityModelingTest;
import org.jboss.tools.bpel.ui.bot.test.AssignActivityTest;
import org.jboss.tools.bpel.ui.bot.test.FaultModelingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ ActivityModelingTest.class, AssignActivityTest.class, FaultModelingTest.class })
@RunWith(RedDeerSuite.class)
public class ModellerTests {

}
