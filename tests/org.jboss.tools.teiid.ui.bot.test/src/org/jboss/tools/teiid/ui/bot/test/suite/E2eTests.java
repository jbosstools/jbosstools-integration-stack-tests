package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.E2eRecursiveXmlTextTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite concentrating on specific features
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({
	E2eRecursiveXmlTextTest.class
})
@RunWith(TeiidSuite.class)
public class E2eTests {

}
