package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.JDBCImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ServerManagementSimpleDV6Test;
import org.jboss.tools.teiid.ui.bot.test.WARTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests with prerequisites (drivers, installed server)
 * Run on all platforms
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	WARTest.class,
	JDBCImportWizardTest.class,
	ServerManagementSimpleDV6Test.class
	//TODO planned tests:
	//DataSourcesTest - TODO add preview operation to JDBCImportWizardTest
	//TeiidConnectionImportTest - TODO solve issues with DV setup
	//AdvancedServerOpsTest - TODO 
})
@RunWith(TeiidSuite.class)
public class ServerTests {

}
