package org.jboss.tools.bpmn2.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.tools.bpmn2.reddder.editor.tests.StartEventSubprocessTest;
import org.jboss.tools.bpmn2.reddder.editor.tests.StartEventTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	StartEventTest.class,
	StartEventSubprocessTest.class
})
@RunWith(BPMN2Suite.class)
public class ImportResourceTests extends TestSuite{

}
