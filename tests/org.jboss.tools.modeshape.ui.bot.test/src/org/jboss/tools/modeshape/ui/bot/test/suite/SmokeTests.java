package org.jboss.tools.modeshape.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.modeshape.ui.bot.test.ModeShapeViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ ModeShapeViewTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
