package org.jboss.tools.drools.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.functional.brms5.Brms5AuditLogTest;
import org.jboss.tools.drools.ui.bot.test.functional.brms5.Brms5ProjectTest;
import org.jboss.tools.drools.ui.bot.test.functional.brms5.Brms5RuntimeManagementTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	// 5.x tests
	Brms5ProjectTest.class, Brms5AuditLogTest.class, Brms5RuntimeManagementTest.class })
public class Brms5TestSuite {

}
