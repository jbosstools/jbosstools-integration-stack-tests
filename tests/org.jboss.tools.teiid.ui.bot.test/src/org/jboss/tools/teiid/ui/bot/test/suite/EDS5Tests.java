package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.ServerManagementSimpleEDS5Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Basic server management operations with EDS5 Run on all platforms
 * 
 * @author lfabriko
 * 
 */
@SuiteClasses({ ServerManagementSimpleEDS5Test.class })
@RunWith(RedDeerSuite.class)
public class EDS5Tests {

}
