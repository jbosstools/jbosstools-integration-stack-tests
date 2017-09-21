package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpmn2.ui.bot.complex.test.testcase.ComplexParalellSplitJoinTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ ComplexParalellSplitJoinTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
