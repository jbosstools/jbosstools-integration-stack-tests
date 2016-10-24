package org.jboss.tools.common.reddeer.view;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

public class ProblemsViewExt extends ProblemsView {

	public void doubleClickProblem (String name, ProblemType type) {
		activate();
		List<TreeItem> items = new DefaultTree().getItems();
		for (TreeItem item : items) {
			if (type.equals(ProblemType.ERROR) && item.getText().startsWith("Errors")) {
				List<TreeItem> tmpList = item.getItems();
				for (TreeItem tmp : tmpList) {
					if (tmp.getText().contains(name)) {
						tmp.doubleClick();
						break;
					}
				}
				break;
			}
		}
	}
}
