package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.GuidesTest;
import org.jboss.tools.teiid.ui.bot.test.PreviewModelTest;
import org.jboss.tools.teiid.ui.bot.test.ProcedurePreviewTest;
import org.jboss.tools.teiid.ui.bot.test.RecursiveCommonTableExpression;
import org.jboss.tools.teiid.ui.bot.test.ReuseVDBTest;
import org.jboss.tools.teiid.ui.bot.test.ServerManipulationTest;
import org.jboss.tools.teiid.ui.bot.test.UDFTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for all teiid bot tests
 * 
 * @author apodhrad, tsedmik
 */
@SuiteClasses({
	PreviewModelTest.class,
	ProcedurePreviewTest.class,
	GuidesTest.class,
	RecursiveCommonTableExpression.class,
	UDFTest.class,
	ServerManipulationTest.class,
	ReuseVDBTest.class})
@RunWith(RedDeerSuite.class)
public class OtherTests {
}
