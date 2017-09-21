package org.jboss.tools.drools.reddeer.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.hamcrest.Matcher;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;

public final class ItemLookup {
	private static final Logger LOGGER = Logger.getLogger(ItemLookup.class);

	public static TreeItem getItemInTree(Tree tree, Matcher<String>... matchers) {
		List<TreeItem> treeItems = tree.getItems();
		TreeItem result = null;
		for (int i = 0; i < matchers.length; i++) {
			for (TreeItem item : treeItems) {
				// this is done because of the debug view - getText() on unseen lines returns empty string
				item.select();
				if (matchers[i].matches(item.getText())) {
					if (i == matchers.length - 1) {
						result = item;
					} else {
						treeItems = item.getItems();
					}
					break;
				}
			}
		}

		return result;
	}

	public static TableItem getItemInTable(Table table, Matcher<String>... matchers) {
		List<TableItem> tableItems = table.getItems();
		TableItem result = null;

		for (int i = 0; i < matchers.length; i++) {
			LOGGER.debug(tableItems);
			for (TableItem item : tableItems) {
				item.select();
				if (matchers[i].matches(item.getText())) {
					if (i == matchers.length - 1) {
						result = item;
					} else {
						item.doubleClick();
						tableItems = table.getItems();
					}
					break;
				}
			}
		}

		return result;
	}
}
