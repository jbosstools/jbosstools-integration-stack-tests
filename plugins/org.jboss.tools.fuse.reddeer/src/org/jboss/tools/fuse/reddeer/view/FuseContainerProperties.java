package org.jboss.tools.fuse.reddeer.view;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Performs operation with <i>Properties View</i> according to JBoss Fuse Fabric
 * Containers
 * 
 * @author tsedmik
 */
public class FuseContainerProperties extends PropertiesView {

	/**
	 * Selects a given container in <i>Properties View</i>
	 * 
	 * @param id
	 *            name of the container
	 */
	public void selectItem(String id) {

		getItem(id).select();
	}

	/**
	 * Checks whether is a container listed in the table
	 * 
	 * @param id
	 *            name of the container
	 * @return <b>true</b> - the container is present, <b>false</b> - otherwise
	 */
	public boolean isContainerPresent(String id) {

		if (getItem(id) != null)
			return true;
		return false;
	}

	/**
	 * Returns status of a given container
	 * 
	 * @param id
	 *            name of the container
	 * @return <b>null</b> - the container is not even listed in the table<br/>
	 *         <b>otherwise</b> - status of the container
	 */
	public String getStatus(String id) {

		TableItem item = getItem(id);
		if (item == null)
			return null;

		return item.getText(8);
	}

	/**
	 * Refreshes <i>Properties View</i>
	 */
	public void refresh() {

		new DefaultToolItem("Refreshes the view").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
	}

	/**
	 * Clicks on the ToolItem 'Start the Container's JVM'
	 */
	public void startContainer() {

		new DefaultToolItem("Start the Container's JVM").click();
		AbstractWait.sleep(TimePeriod.getCustom(10)); // TODO use wait condition on container's status
		new WorkbenchShell().setFocus();
		refresh();
	}

	/**
	 * Checks whether is the ToolItem 'Start the Container's JVM' enabled
	 * 
	 * @return <b>true</b> - the item is enabled<br/>
	 *         <b>false</b> - otherwise
	 */
	public boolean isStartContainerEnabled() {

		return new DefaultToolItem("Start the Container's JVM").isEnabled();
	}

	/**
	 * Clicks on the ToolItem 'Stops the Container's JVM'
	 */
	public void stopContainer() {

		new DefaultToolItem("Stops the Container's JVM").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(10));
		AbstractWait.sleep(TimePeriod.getCustom(2));
		new WorkbenchShell().setFocus();
	}

	/**
	 * Checks whether is the ToolItem 'Stops the Container's JVM' enabled
	 * 
	 * @return <b>true</b> - the item is enabled<br/>
	 *         <b>false</b> - otherwise
	 */
	public boolean isStopContainerEnabled() {

		return new DefaultToolItem("Stops the Container's JVM").isEnabled();
	}

	/**
	 * Clicks on the ToolItem 'Destroys the Container's JVM and configuration in
	 * the Fabric'
	 */
	public void destroyContainer() {

		new DefaultToolItem("Destroys the Container's JVM and configuration in the Fabric").click();
		new DefaultShell().setFocus();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(10));
		AbstractWait.sleep(TimePeriod.getCustom(2));
		new WorkbenchShell().setFocus();
	}

	/**
	 * Checks whether is the ToolItem 'Destroys the Container's JVM and
	 * configuration in the Fabric' enabled
	 * 
	 * @return <b>true</b> - the item is enabled<br/>
	 *         <b>false</b> - otherwise
	 */
	public boolean isDestroyContainerEnabled() {

		return new DefaultToolItem("Destroys the Container's JVM and configuration in the Fabric").isEnabled();
	}

	/**
	 * Searches for a given container in <i>Properties View</i>
	 * 
	 * @param id
	 *            name of the container
	 * @return <b>null</b> - the container is not even listed in the table<br/>
	 *         <b>otherwise</b> - an item corresponding with the name of the
	 *         container
	 */
	private TableItem getItem(String id) {

		List<TableItem> items = new DefaultTable().getItems();
		for (TableItem item : items) {
			if (item.getText(1).equals(id)) {
				return item;
			}
		}

		return null;
	}
}