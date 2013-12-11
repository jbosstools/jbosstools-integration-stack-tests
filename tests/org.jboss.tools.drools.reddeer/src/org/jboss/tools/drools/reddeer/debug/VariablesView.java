package org.jboss.tools.drools.reddeer.debug;

import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.view.View;
import org.jboss.tools.drools.reddeer.util.ItemLookup;

public class VariablesView extends View {

    public VariablesView() {
        super("Debug", "Variables");
    }

    public void selectItem(Matcher<String>... matchers) {
        open();
        ItemLookup.getItemInTree(new DefaultTree(), matchers).select();
    }
}
