package org.jboss.tools.switchyard.ui.bot.test.suite;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.ui.bot.test.DeploymentTest;
import org.jboss.tools.switchyard.ui.bot.test.TopDownBPMN2Test;
import org.jboss.tools.switchyard.ui.bot.test.BindingsTest;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpBPELTest;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpBPMN2Test;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpCamelTest;
import org.jboss.tools.switchyard.ui.bot.test.BottomUpEJBTest;
import org.jboss.tools.switchyard.ui.bot.test.DomainSettingsTest;
import org.jboss.tools.switchyard.ui.bot.test.DroolsTest;
import org.jboss.tools.switchyard.ui.bot.test.UseCaseFileGatewayTest;
import org.jboss.tools.switchyard.ui.bot.test.ImplementationsPropertiesTest;
import org.jboss.tools.switchyard.ui.bot.test.UseCaseSimpleTest;
import org.jboss.tools.switchyard.ui.bot.test.ThrottlingTest;
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
	UseCaseWSProxySOAPTest.class,
	UseCaseWSProxyRESTTest.class,
	UseCaseFileGatewayTest.class,
	BottomUpBPELTest.class,
	BottomUpCamelTest.class,
	BottomUpEJBTest.class,
	BottomUpBPMN2Test.class,
	TopDownBPMN2Test.class,
	DroolsTest.class,
	ImplementationsPropertiesTest.class,
	DomainSettingsTest.class,
	BindingsTest.class,
	ThrottlingTest.class,
	DeploymentTest.class
})
@RunWith(RedDeerSuite.class)
public class AllTests extends TestSuite {

}
