package org.jboss.tools.esb.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.esb.ui.bot.test.ActionsTest;
import org.jboss.tools.esb.ui.bot.test.ListenersTest;
import org.jboss.tools.esb.ui.bot.test.NewESBFileTest;
import org.jboss.tools.esb.ui.bot.test.NotificationsTest;
import org.jboss.tools.esb.ui.bot.test.ProvidersTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	NewESBFileTest.class,
	ActionsTest.class,
	ListenersTest.class,
	ProvidersTest.class,
	NotificationsTest.class })
@RunWith(RedDeerSuite.class)
public class SmokeTests extends TestSuite {

}
