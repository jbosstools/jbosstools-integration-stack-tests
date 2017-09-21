package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ConsumeRestWs;
import org.jboss.tools.teiid.ui.bot.test.ConsumeSoapWs;
import org.jboss.tools.teiid.ui.bot.test.RestProcedureTest;
import org.jboss.tools.teiid.ui.bot.test.WebServiceCreationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ConsumeRestWs.class,
	ConsumeSoapWs.class,
	RestProcedureTest.class,
	WebServiceCreationTest.class})
@RunWith(RedDeerSuite.class)
public class WebServiceTests {}
