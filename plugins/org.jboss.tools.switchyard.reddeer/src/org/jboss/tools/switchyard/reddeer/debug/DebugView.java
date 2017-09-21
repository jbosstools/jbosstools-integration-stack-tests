package org.jboss.tools.switchyard.reddeer.debug;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

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
