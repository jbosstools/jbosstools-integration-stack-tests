package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.BasicTest;
import org.jboss.tools.teiid.ui.bot.test.FlatFileTest;
import org.jboss.tools.teiid.ui.bot.test.ModelRefactoringTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.PerspectiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	PerspectiveTest.class,
	BasicTest.class,
	ModelWizardTest.class,
	ModelRefactoringTest.class,
	FlatFileTest.class })
@RunWith(RedDeerSuite.class)
public class TeiidSmokeTests {

}
