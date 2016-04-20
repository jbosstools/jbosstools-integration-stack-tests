package org.jboss.tools.common.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wait condition - test whether a tree has an item
 * 
 * @author tsedmik
 */
public class TreeHasItem extends AbstractWaitCondition {

	private Tree tree;
	private String[] path;

	/**
	 * Construct tree has item condition.
	 * 
	 * @param tree
	 *            given tree
	 * @param item
	 *            name of an entry of given tree
	 */
	public TreeHasItem(Tree tree, String... path) {
		super();
		this.tree = tree;
		this.path = path;
	}

	@Override
	public boolean test() {
		try {
			new DefaultTreeItem(tree, path);
		} catch (CoreLayerException e) {
			return false;
		}
		return true;
	}

	@Override
	public String description() {
		return "tree has children - " + path[path.length - 1];
	}
}
