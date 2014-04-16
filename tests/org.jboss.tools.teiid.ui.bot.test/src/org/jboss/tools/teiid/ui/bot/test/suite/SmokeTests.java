package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardExtTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.RelationalViewModelTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for smoke teiid bot tests 
 * Test basic functions of teiid designer, tests without prerequisites
 * Run on all platforms
 * 
 * @author apodhrad, lkrejcir
 * 
 */
@SuiteClasses({
	ImportWizardTest.class,
	ModelWizardTest.class,
	ModelWizardExtTest.class,
	RelationalViewModelTest.class,
	//TODO planned tests:
	//XMLDocumentViewModelTest.class
	//RelationalSourceModelTest.class,
})
@RunWith(TeiidSuite.class)
public class SmokeTests {

}
