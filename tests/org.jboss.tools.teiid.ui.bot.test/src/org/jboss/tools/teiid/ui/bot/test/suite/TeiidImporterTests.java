package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.TeiidConnectionImportTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TeiidConnectionImportTest.class	
})
@RunWith(TeiidSuite.class)
public class TeiidImporterTests {

}
