package org.jboss.tools.teiid.ui.bot.test.suite;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.teiid.ui.bot.test.reproducers.TeiidDes2888Test;
import org.jboss.tools.teiid.ui.bot.test.reproducers.TeiidDes3106Test;
import org.jboss.tools.teiid.ui.bot.test.reproducers.TeiidDes3125Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ 
    TeiidDes3125Test.class,
    TeiidDes2888Test.class,
    TeiidDes3106Test.class })
@RunWith(RedDeerSuite.class)
public class ReproducersTest {
}
