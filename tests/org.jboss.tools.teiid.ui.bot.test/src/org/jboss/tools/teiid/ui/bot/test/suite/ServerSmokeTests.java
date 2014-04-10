package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.ServerManagementSimpleDV6Test;
import org.jboss.tools.teiid.ui.bot.test.ServerManagementSimpleEDS5Test;
import org.jboss.tools.teiid.ui.bot.test.WARTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for smoke teiid bot tests
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	//something like test for: creating source model from VDB and other basic ops
	WARTest.class
	//DataSourcesTest - TODO
})
@RunWith(TeiidSuite.class)
public class ServerSmokeTests {

}
