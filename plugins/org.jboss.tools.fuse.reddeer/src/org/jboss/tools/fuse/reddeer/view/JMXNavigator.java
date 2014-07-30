package org.jboss.tools.fuse.reddeer.view;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Performs operations with the Fuse JMX Navigator View
 * 
 * @author tsedmik
 */
public class JMXNavigator extends WorkbenchView {

	public static final String TITLE = "JMX Navigator";
	public static final String CONNECT_CONTEXT_MENU = "Connect...";

	private static final Logger log = Logger.getLogger(JMXNavigator.class);

	public JMXNavigator() {
		super(TITLE);
	}

	/**
	 * Tries to connect to a specified node under <i>Local Processes</i> in
	 * <i>JMX Navigator</i> View.
	 * 
	 * @param name
	 *            prefix name of the node
	 * @return the desired node or null if the searched node is not present
	 */
	public TreeItem connectTo(String name) {

		log.info("Connecting to '" + name + "'.");
		open();
		List<TreeItem> items = new DefaultTree().getItems();
		// temporary hack due to bad process view in JMX Navigator View (should
		// by fix with new JBT)
		for (TreeItem item : items) {
			if (item.getItems().size() > 0) {
				items = item.getItems();
				break;
			}
		}
		for (TreeItem item : items) {
			if (item.getText().startsWith(name)) {
				item.select();
				AbstractWait.sleep(TimePeriod.getCustom(2));
				try {
					new ContextMenu(CONNECT_CONTEXT_MENU).select();
				} catch (SWTLayerException ex) {
					log.info("Already connected to '" + name + "'.");
				}
				AbstractWait.sleep(TimePeriod.getCustom(2));
				item.expand();
				return item;
			}
		}

		return null;
	}

	/**
	 * Tries to decide if a particular node described with the path is in the
	 * tree in <i>JMX Navigator</i> View (below the node <i>Local
	 * Processes</i>).
	 * 
	 * @param path
	 *            Path to the desired node (names of nodes in the tree in <i>JMX
	 *            Navigator</i> View without the <i>Local Processes</i> node.
	 * @return The node if exists, <b>null</b> - otherwise
	 */
	public TreeItem getNode(String... path) {

		open();
		List<TreeItem> items = new DefaultTree().getItems();

		// temporary hack due to bad process view in JMX Navigator View (should
		// by fix with new JBT)
		for (TreeItem item : items) {
			if (item.getItems().size() > 0) {
				items = item.getItems();
				break;
			}
		}
		String[] temp = path;
		if (temp[0].equals("Local Camel Context")) {
			boolean camelContext = false;
			for (TreeItem item : items) {
				if (item.getText().equals("Local Camel Context")) {
					camelContext = true;
				}
			}
			if (!camelContext) {
				temp[0] = "maven";
			}
		}

		for (int i = 0; i < temp.length; i++) {
			for (TreeItem item : items) {
				if (item.getText().startsWith(temp[i])) {

					if (i == temp.length - 1) {
						return item;
					}

					item.select();
					item.doubleClick();
					items = item.getItems();
					break;
				}
			}
		}

		return null;
	}

}
