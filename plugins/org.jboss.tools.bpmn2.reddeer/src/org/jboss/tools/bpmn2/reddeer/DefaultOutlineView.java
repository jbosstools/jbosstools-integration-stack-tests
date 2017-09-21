package org.jboss.tools.bpmn2.reddeer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.hamcrest.Matcher;
import org.jboss.tools.bpmn2.reddeer.matcher.TreeItemWithText;

/**
 * 
 */
public class DefaultOutlineView extends ContentOutline {

	/**
	 * 
	 * @param label
	 */
	public void select(String label) {
		TreeItem itemToSelect = null;

		open();
		itemToSelect = findItem(label);

		if (itemToSelect == null) {
			throw new RuntimeException("Cannot find activity with label '" + label + "'");
		}
		itemToSelect.select();
	}

	/**
	 * 
	 * @param label
	 * @return
	 */
	public TreeItem findItem(String label) {
		return findItem(label, 0);
	}

	/**
	 * 
	 * @param label
	 * @param index
	 * @return
	 */
	public TreeItem findItem(String label, int index) {
		List<TreeItem> foundItems = findItems(new TreeItemWithText(label));
		if (foundItems.isEmpty()) {
			throw new RuntimeException("Cannot find TreeItem with text '" + label + "' at index '" + index + "'");
		}
		return foundItems.get(index);
	}

	/**
	 * 
	 * @param matcher
	 * @return
	 */
	public List<TreeItem> findItems(Matcher<TreeItem> matcher) {
		List<TreeItem> items = new ArrayList<TreeItem>();
		Tree tree = new DefaultTree();
		List<TreeItem> allItems = tree.getAllItems();
		for (TreeItem item : allItems) {
			if (matcher.matches(item)) {
				items.add(item);
			}
		}
		return items;
	}

}
