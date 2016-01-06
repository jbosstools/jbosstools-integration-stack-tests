package org.jboss.tools.drools.reddeer.waitcondition;

import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.api.TreeItem;

public class LoadingTextIsVisible extends AbstractWaitCondition {

	private static final String LOADING_TEXT = "Loading...";

	private TreeItem item;

	public LoadingTextIsVisible(TreeItem item) {
		this.item = item;
	}

	@Override
	public boolean test() {
		List<TreeItem> treeItemList = item.getItems();

		if (treeItemList.size() == 1 && treeItemList.get(0).getText().equals(LOADING_TEXT)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		return "Waiting until 'Loading' text is visible.";
	}

}
