package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.CreateRestProcedureTest;
import org.jboss.tools.teiid.ui.bot.test.ConsumeRestWs;
import org.jboss.tools.teiid.ui.bot.test.ConsumeSoapWs;
import org.jboss.tools.teiid.ui.bot.test.E2eRecursiveXmlTextTest;
import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.JDBCImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.LdapImportTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.TeiidConnectionImportTest;
import org.jboss.tools.teiid.ui.bot.test.TopDownWsdlTest;
import org.jboss.tools.teiid.ui.bot.test.VirtualGroupTutorialTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for all teiid bot tests
 * 
 * @author apodhrad, tsedmik
 */
@SuiteClasses({
	JDBCImportWizardTest.class,
	TeiidConnectionImportTest.class,
	ImportWizardTest.class,
	LdapImportTest.class,
	ModelWizardTest.class,
	ModelRefactoringTest.class,
	CreateRestProcedureTest.class,,
	ConsumeRestWs.class,
	ConsumeSoapWs.class,
	E2eRecursiveXmlTextTest.class,
	RestCallTest.class,
	WARTest.class,
	FlatFileTest.class,
	ProcedurePreviewTest.class,
})
@RunWith(RedDeerSuite.class)
public class AllTests {
}
