package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.BasicTest;
import org.jboss.tools.teiid.ui.bot.test.CloneAndCopyTest;
import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ModelRefactoringTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.TransformationToolsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	CloneAndCopyTest.class,
	TransformationToolsTest.class,
	ImportWizardTest.class,
	BasicTest.class,
	ModelWizardTest.class,
	ModelRefactoringTest.class
})
@RunWith(RedDeerSuite.class)
public class NonServerTests {

}
