package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationDeploymentTest;
import org.jboss.tools.fuse.ui.bot.test.DataTransformationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs tests related to Data Transformation
 * 
 * @author tsedmik
 */
@SuiteClasses({
	DataTransformationTest.class,
	DataTransformationDeploymentTest.class
})
@RunWith(RedDeerSuite.class)
public class DataTransformationTests {

}
