package org.jboss.tools.fuse.reddeer.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.widget.ListElement;

/**
 * Performs operations with the <i>Fabric Explorer</i> view.
 * 
 * TODO Add logging into every method
 * 
 * @author tsedmik
 */
public class FabricExplorer extends DefaultExplorer {
	
	private static final String TITLE = "Fabric Explorer";
	private static final String ADD_BUTTON = "Adds a new Fabric details";
	private static final String NAME = "Name";
	private static final String URL = "Jolokia URL";
	private static final String USERNAME = "User name";
	private static final String PASSWORD = "Password";
	private static final String ZOOPASSWORD = "ZooKeeper Password";
	private static final String NODE_FABRICS = "Fabrics";
	private static final String BTN_REFRESH = "Refreshes the tree";
	private static final String CONTEXT_CONNECT = "Connect";
	
	private static final Logger log = Logger.getLogger(FabricExplorer.class);

	public FabricExplorer() {
		super(TITLE);
	}
	
	/**
	 * Creates a new fabric detail
	 * 
	 * @param name name of a new fabric detail (if is <i>null</i>, a default value is used).
	 * @param url Jolokia URL (if is <i>null</i>, a default value is used).
	 * @param username User name.
	 * @param password Password corresponding with a user.
	 * @param zoopassword ZooKeeper password.
	 */
	public void addFabricDetails(String name, String url, String username, String password, String zoopassword) {
		
		log.info("Creating a new Fuse Fabric detail");
		new DefaultToolItem(ADD_BUTTON).click();
		new DefaultShell().setFocus();
		if (name != null) new LabeledText(NAME).setText(name);
		if (url != null) new LabeledText(URL).setText(url);
		new LabeledText(USERNAME).setText(username);
		new LabeledText(PASSWORD).setText(password);
		new LabeledText(ZOOPASSWORD).setFocus();
		new LabeledText(ZOOPASSWORD).typeText(zoopassword);
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.getCustom(2)); // Wait until a new node is appeared
		log.info("New Fabric detail '" + name +"' was created.");
	}
	
	/**
	 * Tries to connect to a Fabric detail with given name
	 * 
	 * @param name Name of a Fabric detail displayed in <i>Fabric Explorer</i>.
	 *             If it's <i>null</i>, the default value (Local Fabric) is used.
	 */
	public void connectToFabric(String name) {
		
		String fab = name;
		if (fab == null) fab = "Local Fabric";
		log.info("Connecting to the '" + fab + "' fabric");
		selectNode(NODE_FABRICS, fab);
		try {
			selectContextMenuItem(CONTEXT_CONNECT);
		} catch (SWTLayerException ex) {
			log.info("Already connected to '" + fab + "'.");
		}
		new DefaultShell().setFocus();
	}
	
	/**
	 * Creates a new profile under the given path.
	 * 
	 * @param name name of the new profile
	 * @param path a location of the new profile (consider from the node <b>Versions</b> excluded)
	 */
	public void createProfile(String name, String... path) {
		
		List<String> temp = new ArrayList<String>(Arrays.asList(path));
		temp.add(0, "Versions");
		temp.add(0, "Local Fabric");
		temp.add(0, NODE_FABRICS);

		selectNode(temp.toArray(new String[0]));
		selectContextMenuItem("Create a new Profile");
		new DefaultShell().setFocus();
		new DefaultText().setText(name);
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
	}
	
	/**
	 * Deletes a profile defined by path in <i>Fabric Explorer</i> view.
	 * 
	 * @param path complete path to the profile in <i>Fabric Explorer</i> view.
	 */
	public void deleteProfile(String... path) {
		
		selectNode(path);
		deleteProfile();
	}
	
	/**
	 * Deletes selected profile in <i>Fabric Explorer</i> view.
	 */
	public void deleteProfile() {
		
		selectContextMenuItem("Delete Profile");
		new DefaultShell().setFocus();
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		AbstractWait.sleep(TimePeriod.getCustom(2));
		new DefaultShell().setFocus();
	}
	
	/**
	 * Creates a new version
	 * 
	 * @param name Name of the version. Can be null - default value is used
	 */
	public void createVersion(String name) {
		
		selectNode(NODE_FABRICS, "Local Fabric", "Versions");
		selectContextMenuItem("Create Version");
		new DefaultShell().setFocus();
		if (name != null) new DefaultText(0).setText(name);
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new DefaultShell().setFocus();
	}
	
	/**
	 * Creates a new container
	 * 
	 * @param name name of the container
	 * @param version version used by the container
	 * @param profiles profiles assigned to the container
	 */
	public void createContainer(String name, String version, String profile) {
		
		selectNode(NODE_FABRICS, "Local Fabric", "Containers");
		selectContextMenuItem("Create a new child container");
		new DefaultShell().setFocus();

		new LabeledText("Container name").setText(name);
		
		new DefaultCombo().setSelection(version);
		
		List<TreeItem> items = new DefaultTree().getAllItems();
		for (TreeItem item : items) {
			if (profile.equals(item.getText())) {
				item.select();
				break;
			}
		}
		
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new DefaultShell().setFocus();
	}
	
	public void deleteContainer(String name) {
		
		// TODO
	}
	
	/**
	 * Refreshes the tree in <i>Fuse Explorer</i> View.
	 */
	public void refresh() {
		
		log.info("Refreshing the Fuse Explorer tree");
		refresh(BTN_REFRESH);
	}
	
	/**
	 * Deploys a project to a given profile.
	 * 
	 * @param project name of the project in Project Explorer view.
	 * @param profile name of the profile in Fabric Explorer--Local Fabric--Versions--1.0 view.
	 */
	public void deployProjectToProfile(String project, String profile) {
		
		new ProjectExplorer().selectProjects(project);
		new ContextMenu("Deploy to...", "Local Fabric", "1.0", profile).select();
		new WaitUntil(new ConsoleHasText("BUILD SUCCESS"), TimePeriod.LONG);
		open();
	}
	
	/**
	 * Returns a value of <i>FABs</i> of a profile given by a path in <i>Fabric Explorer</i> from
	 * the <b>Versions</b> node (excluded).
	 * 
	 * @param path path to the node.
	 * @return value of <i>FABs</i> property in <i>Properties</i> view.
	 */
	public String getProfileFABs(String... path) {
		
		List<String> temp = new ArrayList<String>(Arrays.asList(path));
		temp.add(0, "Versions");
		temp.add(0, "Local Fabric");
		temp.add(0, NODE_FABRICS);
		
		PropertiesView properties = new PropertiesView();
		properties.open();
		open();
		selectNode(temp.toArray(new String[0]));
		properties.open();

		new ListElement("Details").select();
		String value = new DefaultList(3).getListItems()[0];
		open();
		
		return value;
	}
}
