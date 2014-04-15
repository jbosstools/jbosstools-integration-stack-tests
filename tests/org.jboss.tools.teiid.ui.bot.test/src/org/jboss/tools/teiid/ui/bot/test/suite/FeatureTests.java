package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.BasicTest;
import org.jboss.tools.teiid.ui.bot.test.FlatFileImportTest;
import org.jboss.tools.teiid.ui.bot.test.ServerManagementSimpleTest;
import org.jboss.tools.teiid.ui.bot.test.ServerManagementTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite concentrating on specific features, used e.g. to test a bug presence, run locally 
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	BasicTest.class
	//ServerManagementSimpleTest.class,
	//ServerManagementTest.class
	//FlatFileImportTest.class
})
@RunWith(TeiidSuite.class)
public class FeatureTests {

}
