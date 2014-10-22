package org.jboss.tools.esb.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.esb.ui.bot.test.ActionsTest;
import org.jboss.tools.esb.ui.bot.test.DeploymentTest;
import org.jboss.tools.esb.ui.bot.test.ListenersTest;
import org.jboss.tools.esb.ui.bot.test.NewESBFileTest;
import org.jboss.tools.esb.ui.bot.test.NewProjectUsingRuntimeTest;
import org.jboss.tools.esb.ui.bot.test.NewProjectUsingServerTest;
import org.jboss.tools.esb.ui.bot.test.NotificationsTest;
import org.jboss.tools.esb.ui.bot.test.ProjectExamplesTest;
import org.jboss.tools.esb.ui.bot.test.ProvidersTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ActionsTest.class,
	ListenersTest.class,
	ProvidersTest.class,
	NotificationsTest.class,
	NewESBFileTest.class,
	NewProjectUsingRuntimeTest.class,
	NewProjectUsingServerTest.class,
	DeploymentTest.class,
	ProjectExamplesTest.class
})
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
