package org.jboss.tools.fuse.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Performs operations with the Fuse JMX Navigator View
 * 
 * @author tsedmik
 */
public class FuseJMXNavigator extends WorkbenchView {
	
	public static final String TITLE = "Fuse JMX Navigator";
	public static final String LOCAL_PROCESSES = "Local Processes";
	public static final String REFRESH_BUTTON = "Refreshes the tree";
	public static final String CONNECT_CONTEXT_MENU = "Connect...";
	
	private static final Logger log = Logger.getLogger(FuseJMXNavigator.class);

	public FuseJMXNavigator() {
		super(TITLE);
	}
	
	/**
	 * Accesses names of Local Processes
	 * 
	 * @return List of Local Processes names
	 */
	public List<String> getLocalProcesses() {
		
		open();
		List<String> temp = new ArrayList<String>();
		TreeItem localProcesses = new DefaultTreeItem(LOCAL_PROCESSES);
		log.debug("Accessing Local Processes:");
		for (TreeItem item : localProcesses.getItems()) {
			temp.add(item.getText());
			log.debug(item.getText());
		}
		return temp;
	}
	
	/**
	 * Tries to connect to a specified node under <i>Local Processes</i> in <i>JMX Navigator</i> View.
	 * 
	 * @param name prefix name of the node
	 * @return the desired node or null if the searched node is not present
	 */
	public TreeItem connectTo(String name) {
		
		log.info("Connecting to '" + name + "'.");
		open();
		List<TreeItem> items = new DefaultTreeItem().getItems();
		for (TreeItem item : items) {
			if (item.getText().startsWith(name)) {
				item.select();
				AbstractWait.sleep(TimePeriod.getCustom(2));
				new ContextMenu(CONNECT_CONTEXT_MENU).select();
				AbstractWait.sleep(TimePeriod.getCustom(2));
				item.expand();
				return item;
			}
		}
		
		return null;
	}
	
	/**
	 * Refreshes the tree in <i>JMX Navigator</i> View.
	 */
	public void refresh() {
		
		open();
		TreeItem localProcesses = new DefaultTreeItem(LOCAL_PROCESSES);
		localProcesses.select();
		new DefaultToolItem(REFRESH_BUTTON).click();
		log.info("Refreshing Local Processes in JMX View");
	}
	
	/**
	 * Tries to decide if a particular node described with the path is in the tree in <i>JMX Navigator</i> View 
	 * (below the node <i>Local Processes</i>). 
	 * 
	 * @param path Path to the desired node (names of nodes in the tree in <i>JMX Navigator</i> View without the
	 *             <i>Local Processes</i> node.
	 * @return The node if exists, <b>null</b> - otherwise
	 */
	public TreeItem getNode(String... path) {
		
		open();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		List<TreeItem> items = new DefaultTreeItem().getItems();
		for (int i = 0; i < path.length; i++) {
			for (TreeItem item : items) {
				if (item.getText().startsWith(path[i])) {
					if (i == path.length - 1) {
						return item;
					}
					
					item.select();
					item.doubleClick();
					item.expand();
					AbstractWait.sleep(TimePeriod.getCustom(2));
					items = item.getItems();
					break;
				}
			}
		}
		
		return null;
	}

}
