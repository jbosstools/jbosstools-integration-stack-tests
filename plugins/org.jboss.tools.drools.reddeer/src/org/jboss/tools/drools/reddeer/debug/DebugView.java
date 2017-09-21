package org.jboss.tools.drools.reddeer.debug;

import org.hamcrest.Matcher;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.drools.reddeer.util.ItemLookup;

public class DebugView extends WorkbenchView {
	public DebugView() {
		super("Debug", "Debug");
	}

	public void selectItem(Matcher<String>... matchers) {
		open();
		ItemLookup.getItemInTree(new DefaultTree(), matchers).select();
	}
}
