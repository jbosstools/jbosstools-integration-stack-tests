package org.jboss.tools.bpel.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.hamcrest.Matcher;
import org.jboss.tools.bpel.reddeer.matcher.TreeItemWithText;

/**
 * 
 * @author apodhrad
 *
 */
public class BPELOutlineView extends ContentOutline {

	public void select(String label) {
		TreeItem itemToSelect = null;

		open();
		itemToSelect = findItem(label);

		if (itemToSelect == null) {
			throw new RuntimeException("Cannot find activity with label '" + label + "'");
		}
		itemToSelect.select();
	}

	public TreeItem findItem(String label) {
		return findItem(label, 0);
	}

	public TreeItem findItem(String label, int index) {
		List<TreeItem> foundItems = findItems(new TreeItemWithText(label));
		if (foundItems.isEmpty()) {
			throw new RuntimeException("Cannot find TreeItem with text '" + label + "' at index '" + index + "'");
		}
		return foundItems.get(index);
	}

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
