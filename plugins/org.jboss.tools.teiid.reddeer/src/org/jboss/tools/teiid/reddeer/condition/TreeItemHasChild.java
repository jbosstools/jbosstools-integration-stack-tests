package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.core.matcher.TreeItemTextMatcher;
import org.jboss.reddeer.swt.api.TreeItem;

public class TreeItemHasChild implements WaitCondition {

	private final TreeItem treeItem;
	private TreeItemTextMatcher matcher;
	
	public TreeItemHasChild(TreeItem treeItem, TreeItemTextMatcher matcher) {
		this.treeItem = treeItem;
		this.matcher = matcher;
	}

	@Override
	public boolean test() {
		for (TreeItem it : treeItem.getItems()){
			if (matcher.matches(it.getSWTWidget())){
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
