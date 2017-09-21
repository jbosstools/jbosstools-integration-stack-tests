package org.jboss.tools.switchyard.reddeer.debug;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.common.reddeer.condition.TreeHasItem;

public class VariablesView extends WorkbenchView {

	public VariablesView() {
		super("Variables");
	}

	public String getValue(final String... variablePath) {
		open();
		new WaitUntil(new TreeHasItem(new DefaultTree(), variablePath));
		new DefaultTreeItem(variablePath).select();
		new WaitUntil(new AbstractWaitCondition() {

			@Override
			public boolean test() {
				return new DefaultTreeItem(variablePath).isSelected();
			}

			@Override
			public String description() {
				return "Variable is not selected";
			}
		});
		return new DefaultStyledText().getText();
	}
}
