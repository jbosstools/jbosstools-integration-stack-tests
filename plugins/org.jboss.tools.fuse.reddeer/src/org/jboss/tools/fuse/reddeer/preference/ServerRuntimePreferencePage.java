package org.jboss.tools.fuse.reddeer.preference;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * Represents the Fuse server runtime preference page
 * 
 * @author tsedmik
 */
public class ServerRuntimePreferencePage extends WorkbenchPreferencePage {

	private static final String ADD_BUTTON = "Add...";
	private static final String NEXT_BUTTON = "Next >";
	private static final String FINISH_BUTTON = "Finish";
	private static final String REMOVE_BUTTON = "Remove";
	private static final String EDIT_BUTTON = "Edit...";
	private static final String NEW_WINDOW = "New Server Runtime Environment";
	private static final String EDIT_WINDOW = "Edit Server Runtime Environment";
	private static final String INSTALL_DIR = "Home Directory";
	private static final String NAME = "Name";
	private static final String SERVER_SECTION = "JBoss Fuse";

	public ServerRuntimePreferencePage() {
		super("Server", "Runtime Environments");
	}

	/**
	 * Adds a new server runtime.
	 * 
	 * @param path
	 *            installation directory of a server
	 * @param type
	 *            server type (e.g. "JBoss Fuse", "JBoss Fuse 6.1")
	 */
	public void addServerRuntime(String type, String path) {
		new PushButton(ADD_BUTTON).click();
		new DefaultShell(NEW_WINDOW).setFocus();
		new DefaultTreeItem(SERVER_SECTION, type).select();
		new PushButton(NEXT_BUTTON).click();
		new LabeledText(NAME).setText(type);
		new LabeledText(INSTALL_DIR).setText(path);
		new PushButton(FINISH_BUTTON).click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Removes the server runtime
	 * 
	 * @param name
	 *            name of the removed server runtime
	 */
	public void removeServerRuntime(String name) {
		new DefaultTable(0).select(name);
		new PushButton(REMOVE_BUTTON).click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * Removes all configured server runtimes
	 */
	public void removeAllServerRuntimes() {

		for (String runtime : getServerRuntimes()) {
			removeServerRuntime(runtime);
		}
	}

	/**
	 * Edits the server runtime
	 * 
	 * @param name
	 *            name of the edited server runtime
	 * @param path
	 *            a new installation directory
	 */
	public void editServerRuntime(String name, String path) {
		new DefaultTable(0).select(name);
		new PushButton(EDIT_BUTTON).click();
		new DefaultShell(EDIT_WINDOW).setFocus();
		new LabeledText(INSTALL_DIR).setText(path);
		new PushButton(FINISH_BUTTON).click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	public List<String> getServerRuntimes() {
		List<String> temp = new ArrayList<String>();
		for (TableItem item : new DefaultTable(0).getItems()) {
			temp.add(item.getText());
		}
		return temp;
	}
	
	public void open() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(this);
	}
}
