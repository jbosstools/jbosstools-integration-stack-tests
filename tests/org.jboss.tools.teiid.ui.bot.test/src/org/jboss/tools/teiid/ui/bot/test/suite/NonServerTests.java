package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.CloneAndCopyTest;
import org.jboss.tools.teiid.ui.bot.test.TransformationToolsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	CloneAndCopyTest.class,
	TransformationToolsTest.class
})
@RunWith(RedDeerSuite.class)
public class NonServerTests {

}
