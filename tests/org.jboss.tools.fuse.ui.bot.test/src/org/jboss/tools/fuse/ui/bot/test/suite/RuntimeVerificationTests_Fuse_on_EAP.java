package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationDeploymentEAPTest;
import org.jboss.tools.fuse.ui.bot.test.DeploymentEAPTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * Runs verification tests for a JBoss Fuse on EAP runtime.
 * 
 * @author tsedmik
 */
@SuiteClasses({
	DeploymentEAPTest.class,
	DataTransformationDeploymentEAPTest.class })
@RunWith(RedDeerSuite.class)
public class RuntimeVerificationTests_Fuse_on_EAP extends TestSuite {

}
