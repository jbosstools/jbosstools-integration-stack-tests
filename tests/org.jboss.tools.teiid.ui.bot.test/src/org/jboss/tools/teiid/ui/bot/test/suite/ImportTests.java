package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.FlatFileTest;
import org.jboss.tools.teiid.ui.bot.test.GeometryTypeTest;
import org.jboss.tools.teiid.ui.bot.test.JDBCImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.LdapImportTest;
import org.jboss.tools.teiid.ui.bot.test.SalesforceImportTest;
import org.jboss.tools.teiid.ui.bot.test.TeiidConnectionImportTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
	TeiidConnectionImportTest.class, 
	JDBCImportWizardTest.class, 
	SalesforceImportTest.class, 
	LdapImportTest.class, 
	GeometryTypeTest.class, 
	FlatFileTest.class })
@RunWith(RedDeerSuite.class)
public class ImportTests {}
