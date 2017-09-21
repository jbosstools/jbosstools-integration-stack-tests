package org.jboss.tools.bpel.reddeer.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class QuickPickTree {

	public static final String LABEL_QUICK_PICK = "Quick Pick:";

	private Tree tree;

	public QuickPickTree() {
		tree = new DefaultTree();
	}

	public void pick(String operation) {
		List<TreeItem> operations = findItems(tree.getAllItems(), operation);
		if (operations.isEmpty()) {
			throw new RuntimeException("Cannot find operation with name '" + operation + "'");
		}
		operations.get(0).select();
		operations.get(0).doubleClick();
	}

	public void pick(String[] path) {
		new DefaultTreeItem(path).select();
	}

	private List<TreeItem> findItems(List<TreeItem> treeItems, String label) {
		List<TreeItem> result = new ArrayList<TreeItem>();
		for (TreeItem treeItem : treeItems) {
			if (label.equals(treeItem.getText())) {
				result.add(treeItem);
			}
		}
		return result;
	}

}
