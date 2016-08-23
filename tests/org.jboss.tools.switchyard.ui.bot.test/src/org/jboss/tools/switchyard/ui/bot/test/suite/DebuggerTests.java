package org.jboss.tools.switchyard.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.DebuggerBreakpointTest;
import org.jboss.tools.switchyard.ui.bot.test.DebuggerDebuggingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({ DebuggerBreakpointTest.class, DebuggerDebuggingTest.class })
@RunWith(RedDeerSuite.class)
public class DebuggerTests extends TestSuite {

}
