package org.jboss.tools.drools.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.ui.bot.test.kienavigator.CloneRepoTest;
import org.jboss.tools.drools.ui.bot.test.kienavigator.CreateItemsRestTest;
import org.jboss.tools.drools.ui.bot.test.kienavigator.CreateItemsTest;
import org.jboss.tools.drools.ui.bot.test.kienavigator.OpenKieNavigatorTest;
import org.jboss.tools.drools.ui.bot.test.kienavigator.RepositoryManipulationTest;
import org.jboss.tools.drools.ui.bot.test.kienavigator.ServerConnectionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
    // Kie Navigator tests
    CloneRepoTest.class,
    CreateItemsRestTest.class,
    CreateItemsTest.class,
    OpenKieNavigatorTest.class,
    RepositoryManipulationTest.class,
    ServerConnectionTest.class
})
public class KieNavigatorTestSuite {

}
