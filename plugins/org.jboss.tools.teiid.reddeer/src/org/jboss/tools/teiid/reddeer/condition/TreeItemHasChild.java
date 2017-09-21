package org.jboss.tools.teiid.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.core.matcher.TreeItemTextMatcher;
import org.eclipse.reddeer.swt.api.TreeItem;

public class TreeItemHasChild extends AbstractWaitCondition {

	private final TreeItem treeItem;
	private TreeItemTextMatcher matcher;

	public TreeItemHasChild(TreeItem treeItem, TreeItemTextMatcher matcher) {
		this.treeItem = treeItem;
		this.matcher = matcher;
	}

	@Override
	public boolean test() {
		for (TreeItem it : treeItem.getItems()) {
			if (matcher.matches(it.getSWTWidget())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		return "treeItem " + treeItem.getText() + " has child matching " + matcher.toString();
	}
}
