package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.UseCaseFileGatewayTest;
import org.jboss.tools.switchyard.ui.bot.test.UseCaseSimpleTest;
import org.jboss.tools.switchyard.ui.bot.test.UseCaseWSProxyRESTTest;
import org.jboss.tools.switchyard.ui.bot.test.UseCaseWSProxySOAPTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite
 * 
 * @author apodhrad
 *
 */
@SuiteClasses({
	UseCaseSimpleTest.class,
	UseCaseFileGatewayTest.class,
	UseCaseWSProxySOAPTest.class,
	UseCaseWSProxyRESTTest.class
})
@RunWith(RedDeerSuite.class)
public class UseCaseTests extends TestSuite {

}
