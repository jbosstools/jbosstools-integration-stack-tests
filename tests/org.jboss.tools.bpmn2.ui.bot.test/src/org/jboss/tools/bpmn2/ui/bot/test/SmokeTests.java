package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpmn2.ui.bot.test.testcase.editor.ParallelSplitJoinTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ParallelSplitJoinTest.class,
})
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
