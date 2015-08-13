package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.JDBCImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.SalesforceImportTest;
import org.jboss.tools.teiid.ui.bot.test.TeiidConnectionImportTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
	TeiidConnectionImportTest.class,
	JDBCImportWizardTest.class,
	SalesforceImportTest.class,
	})
@RunWith(RedDeerSuite.class)
public class ImportTests {

}
