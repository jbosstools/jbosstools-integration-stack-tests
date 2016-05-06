package org.jboss.tools.drools.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.drools.reddeer.kienavigator.item.OrgUnitItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.ProjectItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.ServerItem;

public class KieNavigatorView extends WorkbenchView {

	private static final String NO_SERVERS_TEXT = "Use the Servers View to create a new server...";

	public KieNavigatorView() {
		super("Drools", "Kie Navigator");
	}

	/**
	 * Opens Kie Navigator if it is not opened.
	 */
	private void checkAndOpen() {
		if (!isOpened()) {
			open();
		}
	}

	/**
	 * Checks if Kie Navigator contains any server.
	 * 
	 * @return false if no servers are available; true otherwise
	 */
	private boolean isEmpty() {
		checkAndOpen();
		return isLinkToServersViewExists();
	}
	
	/**
	 * Checks if the link to create a new server exists.
	 * @return true if exists, false otherwise
	 */
	public boolean isLinkToServersViewExists() {
		try {
			new DefaultLink(NO_SERVERS_TEXT);
		} catch (CoreLayerException e) {
			return false;
		}
		return true;
	}

	/**
	 * Refreshes Kie Navigator.
	 */
	public void refresh() {
		checkAndOpen();
		new ContextMenu("Refresh");
	}

	/**
	 * Returns list of server placed in Kie Navigator.
	 * 
	 * @return List of Kie Navigator servers.
	 */
	public List<ServerItem> getServers() {
		checkAndOpen();
		List<ServerItem> serversList = new ArrayList<ServerItem>();
		if (!isEmpty()) {
			for (TreeItem item : new DefaultTree().getItems()) {
				serversList.add(new ServerItem(item));
			}
		}
		return serversList;
	}

	/**
	 * Returns server item with specified name.
	 * 
	 * @param name
	 *            Server name.
	 * @return Server item with specified name.
	 */
	public ServerItem getServer(String name) {
		checkAndOpen();
		if (!isEmpty()) {
			for (TreeItem item : new DefaultTree().getItems()) {
				if (item.getText().equals(name)) {
					return new ServerItem(item);
				}
			}
		}
		throw new IllegalArgumentException("No such server: " + name);
	}

	public ServerItem getServer(int number) {
		checkAndOpen();
		if (!isEmpty()) {
			List<TreeItem> items = new DefaultTree().getItems();
			if (number <= items.size()) {
				return new ServerItem(items.get(number));
			}
		}
		throw new IllegalArgumentException("No such server: " + number);
	}

	public OrgUnitItem getOrgUnit(int serverNumber, String orgUnitName) {
		return getServer(serverNumber).getOrgUnit(orgUnitName);
	}

	public RepositoryItem getRepository(int serverNumber, String orgUnitName, String repoName) {
		RepositoryItem ri = getOrgUnit(serverNumber, orgUnitName).getRepository(repoName);
		ri.importRepository();
		return getOrgUnit(serverNumber, orgUnitName).getRepository(repoName);
	}

	public ProjectItem getProject(int serverNumber, String orgUnitName, String repoName, String projectName) {
		return getRepository(serverNumber, orgUnitName, repoName).getProject(projectName);
	}
}
