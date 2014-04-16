package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.ServerManagementSimpleDV6Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Basic server management operations with DV6
 * Run on all platforms
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	ServerManagementSimpleDV6Test.class
})
@RunWith(TeiidSuite.class)
public class DV6Tests {

}
