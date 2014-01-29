package org.jboss.tools.drools.ui.bot.test.functional.view;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.DebugPerspective;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.workbench.view.View;
import org.jboss.tools.drools.reddeer.debug.DebugView;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.After;
import org.junit.Before;

public abstract class ViewTestParent extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(ViewTestParent.class);

    final Class<? extends View> viewClass; 

    public ViewTestParent(Class<? extends View> viewClass) {
        this.viewClass = viewClass;
    }

    @Before
    public void openWorkingMemoryViewInDebug() throws InstantiationException, IllegalAccessException {
        new DebugPerspective().open();
        viewClass.newInstance().open();
        new DroolsPerspective().open();
    }

    @After
    public void terminateDebugRun() {
        closeAllDialogs();
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
}
