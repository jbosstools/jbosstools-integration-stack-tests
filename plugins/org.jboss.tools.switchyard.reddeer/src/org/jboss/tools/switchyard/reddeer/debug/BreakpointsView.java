package org.jboss.tools.switchyard.reddeer.debug;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

/**
 * 
 * @author apodhrad
 * 
 */
public class BreakpointsView extends WorkbenchView {

	public BreakpointsView() {
		super("Breakpoints");
	}

	public boolean isEmpty() {
		open();
		return new DefaultTree().getItems().isEmpty();
	}

	public List<Breakpoint> getBreakpoints() {
		open();
		List<Breakpoint> breakpoints = new ArrayList<Breakpoint>();
		List<TreeItem> treeItems = new DefaultTree().getItems();
		for (TreeItem treeItem : treeItems) {
			breakpoints.add(new Breakpoint(treeItem.getText()));
		}
		return breakpoints;
	}

	public void removeSelectedBreakpoints() {
		open();
		new DefaultToolItem("Remove Selected Breakpoints (Delete)").click();
	}

	public void removeAllBreakpoints() {
		open();
		if (!new DefaultToolItem("Remove All Breakpoints").isEnabled()) {
			close();
			open();
		}
		new DefaultToolItem("Remove All Breakpoints").click();
		new DefaultShell("Remove All Breakpoints").setFocus();
		new PushButton("Yes").click();
	}
}
