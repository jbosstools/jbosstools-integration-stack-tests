package org.jboss.tools.switchyard.reddeer.debug;

import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

public class DebugView extends WorkbenchView {

	public DebugView() {
		super("Debug");
	}

	public String getSelectedText() {
		open();
		return new DefaultTreeExt().getSelectedText();
	}

	private class DefaultTreeExt extends DefaultTree {

		public String getSelectedText() {
			return Display.syncExec(new ResultRunnable<String>() {

				@Override
				public String run() {
					org.eclipse.swt.widgets.TreeItem[] items = swtWidget.getSelection();
					if (items.length > 0) {
						return items[0].getText();
					}
					return null;
				}

			});
		}
	}
}
