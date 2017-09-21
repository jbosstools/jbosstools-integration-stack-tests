package org.jboss.tools.drools.reddeer.debug;

import org.hamcrest.Matcher;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.drools.reddeer.util.ItemLookup;

public class VariablesView extends WorkbenchView {

	public VariablesView() {
		super("Debug", "Variables");
	}

	public void selectItem(Matcher<String>... matchers) {
		open();
		ItemLookup.getItemInTree(new DefaultTree(), matchers).select();
	}
}
