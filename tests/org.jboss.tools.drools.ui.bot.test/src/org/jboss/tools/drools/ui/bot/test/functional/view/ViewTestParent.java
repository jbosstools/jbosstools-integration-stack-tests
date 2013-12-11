package org.jboss.tools.drools.ui.bot.test.functional.view;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.tools.drools.reddeer.debug.DebugView;
import org.jboss.tools.drools.reddeer.debug.VariablesView;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.After;

public class ViewTestParent extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(ViewTestParent.class);

    @After
    public void terminateDebugRun() {
        new DebugPerspective().open();
        DebugView v = new DebugView();
        v.open();
        v.selectItem(new RegexMatchers("DroolsTest.*").getMatchers());
        try {
            new ShellMenu(new RegexMatchers("Run", "Terminate.*").getMatchers()).select();
        } catch (Exception ex) {
            LOGGER.debug("Unable to resume debugging", ex);
        }
    }

    /*
     * This methods searches the debug view and variables list to find ksession. It should not be needed as the agenda should be
     * loaded when rule is opened, but in automated tests, this is unreliable
     */
    protected void selectKsessionVariable() {
        DebugView debug = new DebugView();
        debug.open();
        debug.selectItem(new RegexMatchers("DroolsTest.*", "com\\.sample\\.DroolsTest.*", "Thread \\[main\\].*",
                "DroolsTest\\.main.*").getMatchers());

        VariablesView variables = new VariablesView();
        variables.open();
        variables.selectItem(new RegexMatchers("kSession.*").getMatchers());
    }
}
