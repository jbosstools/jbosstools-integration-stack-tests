package org.jboss.tools.bpel.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpel.ui.bot.test.WizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ WizardTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
