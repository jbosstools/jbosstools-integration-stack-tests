package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.JDBCImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardExtTest;
import org.jboss.tools.teiid.ui.bot.test.RelationalViewModelTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for smoke teiid bot tests, more complicated operations
 * than SmokeTests
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	//RelationalSourceModelTest.class,
	RelationalViewModelTest.class,
	//XMLDocumentViewModelTest.class//TODO not supported yet
})
@RunWith(TeiidSuite.class)
public class SmokeExtTests {

}
