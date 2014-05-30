package org.jboss.tools.fuse.reddeer.view;

import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * Class extracts a common functionality of <i>Fuse JMX Navigator</i> and
 * <i>Fabric explorer</i> views.
 * 
 * TODO Refactor FuseJMXNavigator to extends this class.
 * 
 * @author tsedmik
 */
public class DefaultExplorer extends WorkbenchView {

	public DefaultExplorer(String viewTitle) {
		super(viewTitle);
	}

	/**
	 * Selects a particular node given by a path in the tree.
	 * 
	 * @param path Path to the desired node.
	 */
	public void selectNode(String... path) {
		
		List<TreeItem> items = new DefaultTree().getItems();
		for (int i = 0; i < path.length; i++) {
			for (TreeItem item : items) {
				if (item.getText().startsWith(path[i])) {
					
					item.select();
					if (i == path.length - 1) return;
					AbstractWait.sleep(TimePeriod.getCustom(2));
					item.doubleClick();
					item.expand();
					AbstractWait.sleep(TimePeriod.getCustom(2));
					items = item.getItems();
					break;
				}
			}
		}
		
		throw new SWTLayerException("The node '" + path + "' was not found!");
	}
	
	/**
	 * Refreshes the view
	 * 
	 * @param btnName tooltip of the refresh button
	 */
	protected void refresh(String btnName) {
		
		new DefaultToolItem(btnName).click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}
	
	/**
	 * Selects a particular item in the context menu of <b>already</b> selected tree item.
	 * 
	 * @param name context menu item name
	 */
	public void selectContextMenuItem(String... path) {
		
		new ContextMenu(path).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}
}
