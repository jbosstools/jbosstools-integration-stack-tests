package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog.*;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	// Wizard tests
	// ------------
	// BPMN2GenericModelWizardTest.class,
	// BPMN2ModelWizardTest.class,
	JBPMProcessWizardTest.class,
	JBPMProjectWizardTest.class
	})
public class AllWizardTests extends TestSuite {
}