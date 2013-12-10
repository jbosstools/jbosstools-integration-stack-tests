package org.jboss.tools.fuse.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.view.View;

/**
 * Performs operations with the Fuse JMX Navigator View
 * 
 * @author tsedmik
 */
public class FuseJMXNavigator extends View {
	
	public static final String TITLE = "Fuse JMX Navigator";
	public static final String LOCAL_PROCESSES = "Local Processes";
	public static final String REFRESH_BUTTON = "Refreshes the tree";
	
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
	
	public void refresh() {
		
		open();
		TreeItem localProcesses = new DefaultTreeItem(LOCAL_PROCESSES);
		localProcesses.select();
		new ViewToolItem(REFRESH_BUTTON).click();
		log.info("Refreshing Local Processes in JMX View");
	}

}
