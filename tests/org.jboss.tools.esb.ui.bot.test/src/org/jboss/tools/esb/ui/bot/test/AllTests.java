package org.jboss.tools.esb.ui.bot.test;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
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
	DeploymentTest.class
	// TODO: add test for examples
})
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
