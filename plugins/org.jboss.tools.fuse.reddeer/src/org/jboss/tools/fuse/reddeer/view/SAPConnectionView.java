package org.jboss.tools.fuse.reddeer.view;

import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

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

	public TestDestinationConnection openDestinationTest(String name) {
		selectDestination(name);
		new ContextMenu("Test").select();
		return new TestDestinationConnection().activate();
	}

	public SAPDestinationProperties openDestinationProperties(String name) {
		selectDestination(name);
		return new SAPDestinationProperties();
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

	public TestServerConnection openServerTest(String name) {
		selectServer(name);
		new ContextMenu("Test").select();
		return new TestServerConnection().activate();
	}

	public SAPServerProperties openServerProperties(String name) {
		selectServer(name);
		return new SAPServerProperties();
	}

	public class TestDestinationConnection {

		public static final String TITLE = "Test Destination Connection";

		public TestDestinationConnection activate() {
			new DefaultShell(TITLE);
			return this;
		}

		public void test() {
			activate();
			new PushButton("Test").click();
		}

		public void clear() {
			activate();
			new PushButton("Clear").click();
		}

		public void close() {
			activate();
			new PushButton("Close").click();
			new WaitWhile(new ShellWithTextIsAvailable(TITLE));
		}

		public Text getResultText() {
			activate();
			return new DefaultText();
		}
	}

	public class TestServerConnection {

		public static final String TITLE = "Test Server Connection";

		public TestServerConnection activate() {
			new DefaultShell(TITLE);
			return this;
		}

		public void start() {
			activate();
			new PushButton("Start").click();
		}

		public void stop() {
			activate();
			new PushButton("Stop").click();
		}

		public void clear() {
			activate();
			new PushButton("Clear").click();
		}

		public void close() {
			activate();
			new PushButton("Close").click();
			new WaitWhile(new ShellWithTextIsAvailable(TITLE));
		}

		public Text getResultText() {
			activate();
			return new DefaultText();
		}
	}

}
