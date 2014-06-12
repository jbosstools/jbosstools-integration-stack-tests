package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.TeiidConnectionImportTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests which require server setup via jboss-cli prior to running (deploy
 * driver, add module)
 * 
 * @author lfabriko
 */
@SuiteClasses({
	TeiidConnectionImportTest.class
	// TODO not supported yet
})
@RunWith(TeiidSuite.class)
public class AdvancedServerTests {

}
