package org.jboss.tools.drools.reddeer.view;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.view.WorkbenchView;

public class GlobalDataView extends WorkbenchView {
	private static final Pattern NO_GLOBALS = Pattern.compile("The selected working memory has no globals defined\\.");
	private static final Pattern GLOBAL_NAME = Pattern.compile("[^=]+");

	public GlobalDataView() {
		super("Drools", "Global Data");
	}

	public List<String> getGlobalsList() {
		open();
		List<String> globals = new LinkedList<String>();

		String itemText;
		Matcher m;
		for (TreeItem item : new DefaultTree().getItems()) {
			itemText = item.getText();
			if (NO_GLOBALS.matcher(itemText).matches()) {
				// no globals defined
				break;
			} else {
				m = GLOBAL_NAME.matcher(itemText);
				m.find();
				globals.add(m.group());
			}
		}

		return globals;
	}
}
