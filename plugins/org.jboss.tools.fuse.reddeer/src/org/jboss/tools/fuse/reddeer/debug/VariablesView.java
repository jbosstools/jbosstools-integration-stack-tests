package org.jboss.tools.fuse.reddeer.debug;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Represents 'Variables' view
 * 
 * @author apodhrad
 */
public class VariablesView extends WorkbenchView {

	public VariablesView() {

		super("Variables");
	}

	public String getValue(final String... variablePath) {

		open();
		new DefaultTreeItem(variablePath).select();
		new WaitUntil(new WaitCondition() {

			@Override
			public boolean test() {
				return new DefaultTreeItem(variablePath).isSelected();
			}

			@Override
			public String description() {
				return "Variable is not selected";
			}
		});
		open();
		return new DefaultStyledText().getText();
	}
}
