package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.BasicTest;
import org.jboss.tools.teiid.ui.bot.test.CloneAndCopyTest;
import org.jboss.tools.teiid.ui.bot.test.ConsumeRestWs;
import org.jboss.tools.teiid.ui.bot.test.ConsumeSoapWs;
import org.jboss.tools.teiid.ui.bot.test.CreateRestProcedureTest;
import org.jboss.tools.teiid.ui.bot.test.E2eRecursiveXmlTextTest;
import org.jboss.tools.teiid.ui.bot.test.FlatFileTest;
import org.jboss.tools.teiid.ui.bot.test.GuidesTest;
import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.LdapImportTest;
import org.jboss.tools.teiid.ui.bot.test.ModelRefactoringTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.PreviewModelTest;
import org.jboss.tools.teiid.ui.bot.test.ProcedurePreviewTest;
import org.jboss.tools.teiid.ui.bot.test.UDFTest;
import org.jboss.tools.teiid.ui.bot.test.WARTest;
import org.jboss.tools.teiid.ui.bot.test.XmlFileImportTest;
import org.jboss.tools.teiid.ui.bot.test.XmlSchemalessTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for all teiid bot tests
 * 
 * @author apodhrad, tsedmik
 */
@SuiteClasses({
	BasicTest.class,
	CloneAndCopyTest.class,
	ConsumeRestWs.class,
	ConsumeSoapWs.class,
	CreateRestProcedureTest.class,
	E2eRecursiveXmlTextTest.class,
	FlatFileTest.class,
	GuidesTest.class,
	ImportWizardTest.class,
	LdapImportTest.class,
	ModelRefactoringTest.class,
	ModelWizardTest.class,
	PreviewModelTest.class,
	ProcedurePreviewTest.class,
	WARTest.class,
	XmlFileImportTest.class,
	XmlSchemalessTest.class,
	UDFTest.class})

@RunWith(RedDeerSuite.class)
public class AllTests {
}
