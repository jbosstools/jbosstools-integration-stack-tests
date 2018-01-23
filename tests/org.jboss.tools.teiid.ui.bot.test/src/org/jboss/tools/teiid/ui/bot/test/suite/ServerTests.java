package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.GuidesTest;
import org.jboss.tools.teiid.ui.bot.test.ServerManipulationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ ServerManipulationTest.class, GuidesTest.class })
@RunWith(RedDeerSuite.class)
public class ServerTests {
}
