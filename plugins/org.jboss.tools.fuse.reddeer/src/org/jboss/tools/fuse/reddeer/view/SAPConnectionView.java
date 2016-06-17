package org.jboss.tools.fuse.reddeer.view;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.fuse.reddeer.dialog.SAPTestDestinationDialog;
import org.jboss.tools.fuse.reddeer.dialog.SAPTestServerDialog;

public class SAPConnectionView extends WorkbenchView {

	public static final String TITLE = "SAP Connections";

	public SAPConnectionView() {
		super(TITLE);
	}

	public void selectDestination() {
		open();
		new DefaultTreeItem("Sap Connection Configuration", "Destination Data Store").select();
	}

	public void selectDestination(String name) {
		open();
		new DefaultTreeItem("Sap Connection Configuration", "Destination Data Store", name).select();
	}

	public void newDestination(String name) {
		selectDestination();
		new ContextMenu("New Destination").select();
		new DefaultShell("Create Destination");
		new LabeledText("New Destination Name :").setText(name);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Create Destination"));
	}

	public void deleteDestination(String name) {
		selectDestination(name);
		new ContextMenu("Delete").select();
	}

	public SAPTestDestinationDialog openDestinationTest(String name) {
		selectDestination(name);
		new ContextMenu("Test").select();
		return new SAPTestDestinationDialog().activate();
	}

	public SAPDestinationProperties openDestinationProperties(String name) {
		selectDestination(name);
		SAPDestinationProperties sapDestinationProperties = new SAPDestinationProperties();
		sapDestinationProperties.open();
		return sapDestinationProperties;
	}

	public void selectServer() {
		open();
		new DefaultTreeItem("Sap Connection Configuration", "Server Data Store").select();
	}

	public void selectServer(String name) {
		open();
		new DefaultTreeItem("Sap Connection Configuration", "Server Data Store", name).select();
	}

	public void newServer(String name) {
		selectServer();
		new ContextMenu("New Server").select();
		new DefaultShell("Create Server");
		new LabeledText("New Server Name :").setText(name);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Create Server"));
	}

	public void deleteServer(String name) {
		selectServer(name);
		new ContextMenu("Delete").select();
	}

	public SAPTestServerDialog openServerTest(String name) {
		selectServer(name);
		new ContextMenu("Test").select();
		return new SAPTestServerDialog().activate();
	}

	public SAPServerProperties openServerProperties(String name) {
		selectServer(name);
		SAPServerProperties sapServerProperties = new SAPServerProperties();
		sapServerProperties.open();
		return sapServerProperties;
	}

}
