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
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
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
	 * Removes the fabric details
	 * 
	 * @param name name of the fabric detail (if is <i>null</i>, a default value is used).
	 */
	public void removeFabric(String name) {

		String fabric = name == null ? "Local Fabric" : name;
		log.info("Removing the Fabric detail: " + fabric);
		selectNode(NODE_FABRICS, fabric);
		selectContextMenuItem("Delete Fabric details");
		AbstractWait.sleep(TimePeriod.SHORT);
		log.info("Fabric detail (" + fabric + ") was deleted");
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
			while (!hasSelectedNodeChildren(NODE_FABRICS, fab)) {
				selectContextMenuItem(CONTEXT_CONNECT);
				refresh();
			}
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
		
		log.info("Creating a new profile: '" + name + "'.");
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
		log.info("Profile '" + name + "' was created.");
	}
	
	/**
	 * Deletes a profile defined by path in <i>Fabric Explorer</i> view.
	 * 
	 * @param path complete path to the profile in <i>Fabric Explorer</i> view.
	 */
	public void deleteProfile(String... path) {
		
		log.info("Deleting a profile with the path: " + path);
		selectNode(path);
		deleteProfile();
		log.info("Profile (" + path + ") was deleted");
	}
	
	/**
	 * Deletes selected profile in <i>Fabric Explorer</i> view.
	 */
	public void deleteProfile() {
		
		log.info("Invoking the delete profile method on selected profile.");
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
		
		log.info("Creationg a new version: " + name);
		selectNode(NODE_FABRICS, "Local Fabric", "Versions");
		selectContextMenuItem("Create Version");
		new DefaultShell().setFocus();
		if (name != null) new DefaultText(0).setText(name);
		new PushButton("OK").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new DefaultShell().setFocus();
		log.info("New version '" + name + "' was created");
	}
	
	/**
	 * Creates a new container
	 * 
	 * @param name name of the container
	 * @param version version used by the container
	 * @param profiles profiles assigned to the container
	 */
	public void createContainer(String name, String version, String profile) {
		
		log.info("Creating a new container: " + name);
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
		log.info("New container (" + name + ") was created");
	}
	
	/**
	 * Stops a container with a given name
	 * 
	 * @param name name of the container
	 */
	public void stopContainer(String name) {
		
		log.info("Stopping the container: " + name);
		selectNode(NODE_FABRICS, "Local Fabric", "Containers", name);
		selectContextMenuItem("Stop Container");
		AbstractWait.sleep(TimePeriod.SHORT);
		log.info("The container (" + name + ") was stopped");
	}
	
	
	/**
	 * Starts a container with a given name
	 * 
	 * @param name name of the container
	 */
	public void startContainer(String name) {
		
		log.info("Starting the container: " + name);
		selectNode(NODE_FABRICS, "Local Fabric", "Containers", name);
		selectContextMenuItem("Start Container");
		AbstractWait.sleep(TimePeriod.getCustom(10));
		log.info("The container (" + name + ") was started");
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
		
		log.info("Deploing the project (" + project + ") to the profile (" + profile + ")");
		new ProjectExplorer().selectProjects(project);
		new ContextMenu("Deploy to...", "Local Fabric", "1.0", profile).select();
		new WaitUntil(new ConsoleHasText("BUILD SUCCESS"), TimePeriod.LONG);
		open();
		log.info("Project (" + project + ") was successfully deployed to the profile (" + profile + ")");
	}
	
	/**
	 * Returns a value of <i>FABs</i> of a profile given by a path in <i>Fabric Explorer</i>.
	 * 
	 * @param path path to the node.
	 * @return value of <i>FABs</i> property in <i>Properties</i> view.
	 */
	public String getProfileFABs(String... path) {
		
		log.info("Accessing name of the deployed project on: " + path);		
		PropertiesView properties = new PropertiesView();
		properties.open();
		open();
		selectNode(path);
		properties.open();
		new DefaultShell().setFocus();

		new ListElement("Details").select();
		String value = new DefaultList(3).getListItems()[0];
		open();
		
		return value;
	}
	
	/**
	 * Creates a new Amazon Elastic Compute Cloud (EC2).
	 * 
	 * @param name Name of the cloud
	 * @param identity Identity to use on the cloud provider
	 * @param credential Credential to use on the cloud provider
	 * @param owner Owner of the EC2 account
	 */
	public void createCloudDetail(String name, String identity, String credential, String owner) {
		
		log.info("Create a new Amazon Elastic Compute Cloud (EC2)");
		selectNode("Clouds");
		selectContextMenuItem("Add Cloud details");
		new DefaultShell().setFocus();
		new LabeledText("Name").typeText(name);
		new LabeledCombo("Provider name").setSelection("Amazon Elastic Compute Cloud (EC2)");
		new LabeledText("Identity").typeText(identity);
		new LabeledText("Credential").typeText(credential);
		new LabeledText("Owner").typeText(owner);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(300));
		new DefaultShell().setFocus();
		log.info("New cloud details '" + name + "' created.");
	}
	
	/**
	 * Creates a new Fabric in the cloud
	 * 
	 * @param cloudName Name of the cloud 
	 * @param name Name of the Fabric
	 * @param password the password of the user
	 * @param zooPassword the ZooKeeper password for the Fabric
	 * @param location Location ID (e.g. 'eu-west-1')
	 * @param hardware Hardware ID (e.g. 't1.micro')
	 * @param os OS Family (e.g. 'rhel')
	 * @param osVersion OS Version (e.g. '6')
	 */
	public void createFabricInTheCloud(String cloudName, String name, String password, String zooPassword,
			String location, String hardware, String os, String osVersion) {
		
		log.info("Create a Fabric in the Cloud");
		selectNode("Fabrics");
		selectContextMenuItem("Create Fabric in the cloud");
		new DefaultShell().setFocus();
		new DefaultTableItem(cloudName).select();
		new PushButton("Next >").click();
		new DefaultShell().setFocus();
		new LabeledText("Fabric Name").setText(name);
		new LabeledText("Password").setText(password);
		new LabeledText("Zookeeper Password").setText(zooPassword);
		new LabeledCombo("Location ID").setSelection(location);
		new LabeledCombo("Hardware ID").setSelection(hardware);
		new LabeledCombo("OS Family").setSelection(os);
		new LabeledCombo("OS Version").setSelection(osVersion);
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(1800));
		new DefaultShell().setFocus();
		log.info("New Fabric in the cloud '" + name + "' created.");
	}
}
