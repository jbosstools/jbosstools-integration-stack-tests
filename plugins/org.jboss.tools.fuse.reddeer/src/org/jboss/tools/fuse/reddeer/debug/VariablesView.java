package org.jboss.tools.fuse.reddeer.debug;

import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
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
		new DefaultTree().getItems();
		AbstractWait.sleep(TimePeriod.SHORT);
		List<TreeItem> items = new DefaultTree().getItems();
		for (int i = 0; i < variablePath.length; i++) {
			boolean found = false;
			for (TreeItem item : items) {
				if (item.getText().equals(variablePath[i]) && !(i == variablePath.length - 1)) {
					item.expand(TimePeriod.SHORT);
					items = item.getItems();
					found = true;
					break;
				}
				if (item.getText().equals(variablePath[i]) && (i == variablePath.length - 1)) {
					item.select();
					found = true;
					break;
				}
			}
			if (!found) {
				return null;
			}
		}
		AbstractWait.sleep(TimePeriod.SHORT);
		activate();
		return new DefaultStyledText().getText();
	}
}
