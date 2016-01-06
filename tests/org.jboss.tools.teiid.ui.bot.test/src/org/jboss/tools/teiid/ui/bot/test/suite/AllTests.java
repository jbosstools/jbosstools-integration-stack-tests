package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ConsumeRestWs;
import org.jboss.tools.teiid.ui.bot.test.ConsumeSoapWs;
import org.jboss.tools.teiid.ui.bot.test.CreateRestProcedureTest;
import org.jboss.tools.teiid.ui.bot.test.E2eRecursiveXmlTextTest;
import org.jboss.tools.teiid.ui.bot.test.FlatFileTest;
import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.LdapImportTest;
import org.jboss.tools.teiid.ui.bot.test.ModelRefactoringTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ProcedurePreviewTest;
import org.jboss.tools.teiid.ui.bot.test.RestCallTest;
import org.jboss.tools.teiid.ui.bot.test.WARTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for all teiid bot tests
 * 
 * @author apodhrad, tsedmik
 */
@SuiteClasses({
	ImportWizardTest.class,
	LdapImportTest.class,
	ModelWizardTest.class,
	ModelRefactoringTest.class,
	CreateRestProcedureTest.class,
	RestCallTest.class,
	FlatFileTest.class,
	ProcedurePreviewTest.class,
	WARTest.class,
	ConsumeRestWs.class,
	ConsumeSoapWs.class,
	E2eRecursiveXmlTextTest.class, })
@RunWith(RedDeerSuite.class)
public class AllTests {
}
