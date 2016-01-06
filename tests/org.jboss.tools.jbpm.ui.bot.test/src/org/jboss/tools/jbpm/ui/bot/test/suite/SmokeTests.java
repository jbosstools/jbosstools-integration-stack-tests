package org.jboss.tools.jbpm.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.jbpm.ui.bot.test.GPDPaletteTest;
import org.jboss.tools.jbpm.ui.bot.test.GPDTest;
import org.jboss.tools.jbpm.ui.bot.test.JBPMProjectTest;
import org.jboss.tools.jbpm.ui.bot.test.PerspectiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ PerspectiveTest.class, JBPMProjectTest.class, GPDTest.class, GPDPaletteTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
