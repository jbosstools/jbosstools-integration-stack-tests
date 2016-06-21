package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ConsumeRestWs;
import org.jboss.tools.teiid.ui.bot.test.ConsumeSoapWs;
import org.jboss.tools.teiid.ui.bot.test.CreateRestProcedureTest;
import org.jboss.tools.teiid.ui.bot.test.WARTest;
import org.jboss.tools.teiid.ui.bot.test.WebServiceCreationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ConsumeRestWs.class,
	ConsumeSoapWs.class,
	CreateRestProcedureTest.class,
	WebServiceCreationTest.class})
@RunWith(RedDeerSuite.class)
public class WebServiceTests {

}
