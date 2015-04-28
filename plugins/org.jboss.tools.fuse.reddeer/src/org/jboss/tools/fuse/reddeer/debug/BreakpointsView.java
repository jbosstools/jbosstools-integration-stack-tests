package org.jboss.tools.fuse.reddeer.debug;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Represents Breakpoints View.
 * 
 * @author tsedmik
 */
public class BreakpointsView extends WorkbenchView {

	private static Logger log = Logger.getLogger(BreakpointsView.class);

	public BreakpointsView() {

		super("Breakpoints");
	}

	/**
	 * Gets a particular breakpoint in Breakpoints view
	 * 
	 * @param label
	 *            Label or some label's substring of the breakpoint
	 * @return breakpoint - there is some breakpoint with given label, null -
	 *         otherwise
	 */
	public Breakpoint getBreakpoint(String label) {

		log.debug("Accessing breakpoints in Breakpoints view");
		open();
		AbstractWait.sleep(TimePeriod.SHORT);
		List<TreeItem> items = new DefaultTree().getItems();
		for (TreeItem item : items) {
			log.debug("		found: " + item.getText());
			if (item.getText().contains(label)) {
				return new Breakpoint(item.getText());
			}
		}
		return null;
	}

	/**
	 * Checks whether a breakpoint is present in Breakpoints View
	 * 
	 * @param label
	 *            Label or some label's substring of the breakpoint
	 * @return true - breakpoint is present, false - otherwise
	 */
	public boolean isBreakpointSet(String label) {

		log.debug("Checking state of the breakpoint: " + label);
		open();
		return getBreakpoint(label) == null ? false : true;
	}

	/**
	 * Removes all set breakpoints
	 */
	public void removeAllBreakpoints() {

		open();
		new ViewToolItem("Remove All Breakpoints").click();
		if (new ShellWithTextIsAvailable("Remove All Breakpoints").test()) {
			new DefaultShell("Remove All Breakpoints").setFocus();
			new PushButton("Yes").click();
		}
	}

	/**
	 * Imports breakpoints from a file
	 * 
	 * @param path path to the file
	 */
	public void importBreakpoints(String path) {

		open();
		new DefaultTree(0).setFocus();
		new ContextMenu("Import Breakpoints...").select();
		new WaitUntil(new ShellWithTextIsAvailable("Import Breakpoints"));
		new DefaultShell("Import Breakpoints");
		new LabeledText("From file:").setText(path);
		new PushButton("Finish").click();
	}
}
