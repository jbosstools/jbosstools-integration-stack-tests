package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.XmlFileImportTest;
import org.jboss.tools.teiid.ui.bot.test.XmlRecursiveTest;
import org.jboss.tools.teiid.ui.bot.test.XmlSchemalessTest;
import org.jboss.tools.teiid.ui.bot.test.XmlStagingTableTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;



@SuiteClasses({
	XmlSchemalessTest.class,
	XmlStagingTableTest.class,
	XmlRecursiveTest.class,
	XmlFileImportTest.class
})
@RunWith(RedDeerSuite.class)
public class XmlTests {

}
