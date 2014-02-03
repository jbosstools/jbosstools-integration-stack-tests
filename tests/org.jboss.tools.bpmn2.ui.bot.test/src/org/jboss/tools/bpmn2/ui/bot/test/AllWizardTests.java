package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog.*;

/**
 * 
 * @author Marek Baluch
 */
@RunWith(BPMN2Suite.class)
@SuiteClasses({
// Wizard tests
// ------------
//	BPMN2GenericModelWizardTest.class,
//	BPMN2ModelWizardTest.class,
//	JBPMProcessWizardTest.class,
//	JBPMProjectWizardTest.class,
	JBPMMavenProjectWizardTest.class,
})
public class AllWizardTests extends TestSuite {
}